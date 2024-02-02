package com.ssafy.jansorry.batch.config;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.favorite.service.FavoriteBatchService;
import com.ssafy.jansorry.follow.service.FollowBatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchSchedulerConfig {
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final FollowBatchService followBatchService;
	private final FavoriteBatchService favoriteBatchService;

	@Scheduled(cron = "0 0 18 * * ?")// UTC 오후 6시 = KST 오전 3시
	// @Scheduled(fixedRate = 60000) // 60,000밀리초 = 60초
	public void runBatchJob() {
		log.info("info log = {}", "=================== 동기화 작업 시작 ===================");
		JobParameters jobParameters = new JobParametersBuilder()
			.addDate("date", new Date())
			.toJobParameters();
		try {
			Job synchronizeJob = job();
			jobLauncher.run(synchronizeJob, jobParameters);
		} catch (Exception e) {
			log.error("error log = {}", "exception 발생");
			throw new BaseException(BATCH_FAILED);
		}
		log.info("info log = {}", "=================== 동기화 작업 종료 ===================");
	}

	@Bean
	public Job job() {
		log.trace("trace log = {}", "[job 시작]");
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
				log.error("error log = {}", "팔로우 동기화 exception 발생");
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
				log.error("error log = {}", "좋아요 동기화 exception 발생");
				throw new BaseException(FAVORITE_SYNC_FAILED);
			}
			return RepeatStatus.FINISHED;
		};
	}
}
