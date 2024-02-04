package com.ssafy.jansorry.batch.config;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.transaction.PlatformTransactionManager;

import com.ssafy.jansorry.batch.service.BatchService;
import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.favorite.service.FavoriteBatchService;
import com.ssafy.jansorry.follow.service.FollowBatchService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ConditionalBatchScheduler {
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final TaskScheduler taskScheduler;
	private final FollowBatchService followBatchService;
	private final FavoriteBatchService favoriteBatchService;
	private final BatchService batchService;
	private ScheduledFuture<?> futureTask;

	@PostConstruct
	public void scheduleTasks() {
		// 정기적으로 실행될 배치 작업 스케줄링 -> 새벽 세시마다 redis to mysql 반영 작업
		futureTask = taskScheduler.schedule(this::regularBatchJob,
			new CronTrigger("0 0 3 * * ?", ZoneId.of("Asia/Seoul")));

		// 최종 작업 스케줄링 (2024년 2월 13일 새벽 3시 10분)
		taskScheduler.schedule(this::finalBatchJob,
			Date.from(LocalDateTime.of(2024, 2, 13, 3, 10).atZone(ZoneId.of("Asia/Seoul")).toInstant()));

		// // test
		// // `regularBatchJob` 60초 후 시작, 그 후 매 60초마다 실행
		// PeriodicTrigger regularTrigger = new PeriodicTrigger(60, TimeUnit.SECONDS);
		// regularTrigger.setInitialDelay(60); // 첫 실행을 60초 지연
		// futureTask = taskScheduler.schedule(this::regularBatchJob, regularTrigger);
		//
		// // `finalBatchJob` 70초 후 시작, 그 후 매 70초마다 실행
		// PeriodicTrigger finalTrigger = new PeriodicTrigger(70, TimeUnit.SECONDS);
		// finalTrigger.setInitialDelay(70); // 첫 실행을 70초 지연
		// taskScheduler.schedule(this::finalBatchJob, finalTrigger);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// 정기적으로 실행될 작업
	private void regularBatchJob() {
		log.info("info log = {}", "=================== 동기화 작업 시작 ===================");
		JobParameters jobParameters = new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters();
		try {
			Job synchronizeJob = synchronizeJob();
			jobLauncher.run(synchronizeJob, jobParameters);
		} catch (Exception e) {
			log.error("error log = {}", e.getMessage());
			throw new BaseException(BATCH_FAILED);
		}
		log.info("info log = {}", "=================== 동기화 작업 종료 ===================");
	}

	@Bean
	public Job synchronizeJob() {
		log.trace("trace log = {}", "[regular job 시작]");
		return new JobBuilder("synchronizeJob", jobRepository)
			.start(synchronizeFollowStep())
			.next(synchronizeFavoriteStep())
			.build();
	}

	public Step synchronizeFollowStep() {
		log.trace("trace log = {}", "[팔로우 동기화 step 시작]");
		return new StepBuilder("synchronizeFollowStep", jobRepository)
			.tasklet(followTasklet(), transactionManager)
			.build();
	}

	public Step synchronizeFavoriteStep() {
		log.trace("trace log = {}", "[좋아요 동기화 step 시작]");
		return new StepBuilder("synchronizeFavoriteStep", jobRepository)
			.tasklet(favoriteTasklet(), transactionManager)
			.build();
	}

	// Follow 작업
	public Tasklet followTasklet() {
		return (contribution, chunkContext) -> {
			try {
				Set<String> updatedFromIds = followBatchService.synchronizeUpdatedData(
					LocalDateTime.now().minusDays(1));// 이전 배치 시간 = 현재 시간 -1일
					// LocalDateTime.now().minusSeconds(60));// 60초 테스트
				followBatchService.deleteEmptySet(updatedFromIds);
				followBatchService.refreshZSetAfterBatch();
			} catch (Exception e) {
				log.error("팔로우 동기화 오류 발생 = {}", e.getMessage());
				throw new BaseException(FOLLOW_SYNC_FAILED);
			}
			return RepeatStatus.FINISHED;
		};
	}

	// Favorite 작업
	public Tasklet favoriteTasklet() {
		return (contribution, chunkContext) -> {
			try {
				Set<String> updatedActionIds = favoriteBatchService.synchronizeUpdatedData(
					LocalDateTime.now().minusDays(1));// 이전 배치 시간 = 현재 시간 -1일
					// LocalDateTime.now().minusSeconds(60));// 60초 테스트
				favoriteBatchService.deleteEmptySet(updatedActionIds);
				favoriteBatchService.refreshZSetAfterBatch();
			} catch (Exception e) {
				log.error("좋아요 동기화 오류 발생 = {}", e.getMessage());
				throw new BaseException(FAVORITE_SYNC_FAILED);
			}
			return RepeatStatus.FINISHED;
		};
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// 2/13 최종 배치작업
	private void finalBatchJob() {// 최종 데이터 정리 작업
		log.info("info log = {}", "=================== 최종 작업 시작 ===================");
		JobParameters jobParameters = new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters();
		try {
			Job finalJob = finalJob();
			jobLauncher.run(finalJob, jobParameters);
		} catch (Exception e) {
			log.error("error log = {}", e.getMessage());
			throw new BaseException(BATCH_FAILED);
		}

		log.info("info log = {}", "=================== 최종 작업 종료 ===================");
		// 이 작업이 끝나고 나면 더 이상 정기적인 배치 작업을 실행하지 않음
		if (futureTask != null && !futureTask.isCancelled()) {
			futureTask.cancel(false);
			futureTask = null;
		}
		log.info("info log = {}", "=================== 배치 작업 종료 ===================");
	}

	@Bean
	public Job finalJob() {
		log.trace("trace log = {}", "[final job 시작]");
		return new JobBuilder("finalJob", jobRepository)
			.start(finalDataGatheringStep())
			.next(finalDataBindingStep())
			.build();
	}

	public Step finalDataGatheringStep() {
		log.trace("trace log = {}", "[최종 데이터 정리 step 시작]");
		return new StepBuilder("finalDataGatheringStep", jobRepository)
			.tasklet(dataGatheringTasklet(), transactionManager)
			.build();
	}

	public Step finalDataBindingStep() {
		log.trace("trace log = {}", "[최종 데이터 반영 step 시작]");
		return new StepBuilder("finalDataBindingStep", jobRepository)
			.tasklet(dataBindingingTasklet(), transactionManager)
			.build();
	}

	// Follow 작업
	public Tasklet dataGatheringTasklet() {
		return (contribution, chunkContext) -> {
			try {
				// 잔소리
				batchService.updateTop5NagsByGender();// 성별 탑 5위 잔소리 = 2 x 7 = 14
				batchService.updateTop5NagsByAgeRange();// 연령 별 탑 5위 잔소리 = 3 * 7 = 21
				batchService.updateTop5NagsByAll();// 전체 중 탑 5위 잔소리 = 7
				// 좋아요
				batchService.updateTop5ActionByFavoriteCount();// 좋아요 순 탑 5위 actionId = 1
				// 영수증
				batchService.updateTop5ReceiptsByPrice();// 가격 순 탑 5위 영수증 = 1
				// ======== 최종 44개의 데이터 풀 updated finished on redis db idx "3" ========
			} catch (Exception e) {
				log.error("최종 데이터 정리 중 오류 발생 = {}", e);
				throw new BaseException(DATA_GATHERING_FAILED);
			}
			return RepeatStatus.FINISHED;
		};
	}

	// Favorite 작업
	public Tasklet dataBindingingTasklet() {
		return (contribution, chunkContext) -> {
			try {
				batchService.bindRedisToMysql();
			} catch (Exception e) {
				log.error("최종 데이터 반영 중 오류 발생 = {}", e);
				throw new BaseException(DATA_BINDING_FAILED);
			}
			return RepeatStatus.FINISHED;
		};
	}
}
