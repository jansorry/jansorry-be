-- gender insert
INSERT INTO gender (gender_type, created_at, updated_at)
VALUES
    ('MALE', NOW(), NOW()),
    ('FEMALE', NOW(), NOW()),
    ('UNDISCLOSED', NOW(), NOW()),
    ('UNKNOWN', NOW(), NOW());

-- ------------------------------------------------------------------------

-- category insert
INSERT INTO category (group_type, created_at, updated_at)
VALUES
    ('LEARNING', NOW(), NOW()),
    ('APPEARANCE', NOW(), NOW()),
    ('LOVE', NOW(), NOW()),
    ('CAREER', NOW(), NOW()),
    ('FINANCE', NOW(), NOW()),
    ('OTHER', NOW(), NOW());

-- ------------------------------------------------------------------------
-- nag insert

INSERT INTO nag (category_id, content, price, deleted, created_at, updated_at)
VALUES
-- 학업 · 진로 (Category ID: 1)
(1, '대학은 어디 갈거니?', 50000, FALSE, NOW(), NOW()),
(1, '군대는 도대체 언제 갈거니?', 50000, FALSE, NOW(), NOW()),
(1, '학점(학교 성적)은 잘 나오니?', 60000, FALSE, NOW(), NOW()),
(1, '언제까지 그렇게 놀거니?', 40000, FALSE, NOW(), NOW()),
(1, '이번 모의고사 몇 등급이니?', 30000, FALSE, NOW(), NOW()),
(1, '뭐 되려고 이러니?', 40000, FALSE, NOW(), NOW()),
(1, '그 대학 간 거 만족하니?', 50000, FALSE, NOW(), NOW()),
(1, 'ㅇㅇ이는 1등 했다더라', 60000, FALSE, NOW(), NOW()),
(1, '어릴 때는 똘똘했는데…', 70000, FALSE, NOW(), NOW()),

-- 건강 · 외모 (Category ID: 2)
(2, '이 부위만 성형하면 예쁠텐데', 50000, FALSE, NOW(), NOW()),
(2, '얼굴이 한 물 갔네..', 80000, FALSE, NOW(), NOW()),
(2, '키가 더 컸으면 좋았을 텐데..', 70000, FALSE, NOW(), NOW()),
(2, '너네 나이 때는 화장 안 해도 예뻐', 60000, FALSE, NOW(), NOW()),
(2, '대학도 갔는데 좀 꾸며라', 50000, FALSE, NOW(), NOW()),
(2, '피부가 왜 그래?', 70000, FALSE, NOW(), NOW()),
(2, '살 빼면 예쁘겠네/잘생겨지겠네', 70000, FALSE, NOW(), NOW()),
(2, '혹시 너 탈모니?', 60000, FALSE, NOW(), NOW()),
(2, '이 문신은 뭐니?', 30000, FALSE, NOW(), NOW()),
(2, '담배 좀 끊어라', 30000, FALSE, NOW(), NOW()),

-- 연애 · 결혼 (Category ID: 3)
(3, '너 연애는 안 하니?', 40000, FALSE, NOW(), NOW()),
(3, '그러니까 연애를 못하지', 50000, FALSE, NOW(), NOW()),
(3, '(애인)은 직장 어디 다니니?', 40000, FALSE, NOW(), NOW()),
(3, '(애인)이랑 결혼 할꺼니?', 50000, FALSE, NOW(), NOW()),
(3, '(애인) 언제 소개시켜줄거니?', 50000, FALSE, NOW(), NOW()),
(3, '(애인) 부모님은 뭐하시니?', 80000, FALSE, NOW(), NOW()),
(3, '(애인)이 보살이다.', 40000, FALSE, NOW(), NOW()),
(3, '이제 ㅇㅇ이도 결혼할 나이지.', 60000, FALSE, NOW(), NOW()),
(3, '혼수는 뭐 해온대?', 70000, FALSE, NOW(), NOW()),
(3, '신혼 집이 월세(전세)니?', 80000, FALSE, NOW(), NOW()),
(3, '맞선 안 볼래?', 70000, FALSE, NOW(), NOW()),
(3, '결혼 안 하면 노후에 쓸쓸해서 어떡해', 50000, FALSE, NOW(), NOW()),
(3, '완전 도둑놈 아니야?? 양심 없네', 30000, FALSE, NOW(), NOW()),

-- 취업 · 직장 (Category ID: 4)
(4, 'ㅇㅇ는 회사에서 승진했다던데 넌 언제해?', 70000, FALSE, NOW(), NOW()),
(4, '차라리 기술 배우는 건 어때?', 60000, FALSE, NOW(), NOW()),
(4, '알바는 안하니?', 40000, FALSE, NOW(), NOW()),
(4, '그게 스펙에 도움이 돼?', 60000, FALSE, NOW(), NOW()),
(4, '취업은 언제쯤 하려고?', 100000, FALSE, NOW(), NOW()),
(4, '너 친구들은 뭐해?', 80000, FALSE, NOW(), NOW()),
(4, '그 과, 취직은 되니?', 60000, FALSE, NOW(), NOW()),
(4, '눈만 낮추면 갈 데가 얼마나 많은데', 80000, FALSE, NOW(), NOW()),
(4, '연봉은 얼마나 받니?', 80000, FALSE, NOW(), NOW()),
(4, '그 회사 계속 다닐 거니?', 60000, FALSE, NOW(), NOW()),
(4, '졸업은 언제 할 생각이니?', 50000, FALSE, NOW(), NOW()),
(4, '그 회사 비전은 있니?', 90000, FALSE, NOW(), NOW()),
(4, 'ㅇㅇ는 대기업 취직했더라', 90000, FALSE, NOW(), NOW()),
(4, '아직도 용돈 받니?', 70000, FALSE, NOW(), NOW()),

-- 가족 · 자녀 (Category ID: 5)
(5, '돈 많이 벌어서 키워 준 값은 해야지', 80000, FALSE, NOW(), NOW()),
(5, '가족들한테 잘해라', 70000, FALSE, NOW(), NOW()),
(5, '네가 이 집안의 가장이야', 80000, FALSE, NOW(), NOW()),
(5, '다 너 잘되라고 하는 소리야', 90000, FALSE, NOW(), NOW()),
(5, '첫째니까 네가 잘 챙겨야지', 70000, FALSE, NOW(), NOW()),
(5, '공부도 못하는데 집안일은 네가 해야지', 80000, FALSE, NOW(), NOW()),
(5, '연락 좀 자주 해라', 40000, FALSE, NOW(), NOW()),
(5, 'ㅇㅇ는 매달 돈 보낸다더라', 80000, FALSE, NOW(), NOW()),
(5, '결혼 안 할 거면 부모님은 네가 모셔', 90000, FALSE, NOW(), NOW()),
(5, '집에서 밥은 해먹고 다니니?', 70000, FALSE, NOW(), NOW()),
(5, 'ㅇㅇ는 양가 도움없이 알아서 결혼했더라', 80000, FALSE, NOW(), NOW()),
(5, '내가 자식 집에 오는데 허락을 맡아야 되니?', 100000, FALSE, NOW(), NOW()),
(5, '가만히 있지 말고 엄마 좀 도와라', 40000, FALSE, NOW(), NOW()),
(5, '애 가질 때 되지 않았니?', 60000, FALSE, NOW(), NOW()),
(5, '딸이 있어야 안 외롭지', 50000, FALSE, NOW(), NOW()),
(5, '아들이 있어야 든든하지', 50000, FALSE, NOW(), NOW()),
(5, '요즘 애들은 애도 안 낳고', 60000, FALSE, NOW(), NOW()),
(5, '우리 때는 없는 살림에도 애 키웠어', 70000, FALSE, NOW(), NOW()),

-- 기타 (Category ID: 6)
(6, '이제 독립할 때 되지 않았어?', 70000, FALSE, NOW(), NOW()),
(6, '너 아직도 코인 하니?', 10000, FALSE, NOW(), NOW()),
(6, '돈은 많이 모았어?', 90000, FALSE, NOW(), NOW()),
(6, '반려 동물에 돈 너무 많이 쓴다', 80000, FALSE, NOW(), NOW()),
(6, '종교 생활 열심히 해야지', 60000, FALSE, NOW(), NOW()),
(6, '너 친한 친구는 있어?', 60000, FALSE, NOW(), NOW()),
(6, '아직도 아이돌 따라다니니?', 70000, FALSE, NOW(), NOW()),
(6, '여자/남자답게 굴어야지~', 60000, FALSE, NOW(), NOW()),
(6, '게임 좀 그만해라', 70000, FALSE, NOW(), NOW()),
(6, '핸드폰 좀 그만해라', 70000, FALSE, NOW(), NOW()),
(6, 'ㅇㅇ이는 주식으로 몇 천만원 벌었다던데.', 60000, FALSE, NOW(), NOW()),
(6, '그 때 그 아파트 몇 억 뛰었다더라', 70000, FALSE, NOW(), NOW());

-- ------------------------------------------------------------------------

