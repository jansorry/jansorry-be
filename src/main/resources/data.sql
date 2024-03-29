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
    ('FAMILY', NOW(), NOW()),
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
-- message insert

INSERT INTO message (nag_id, content, deleted, created_at, updated_at)
VALUES
    (1, '대학진학은 신중히 고민중이에요.', FALSE, NOW(), NOW()),
    (1, '최선의 진학 선택을 할 거예요!', FALSE, NOW(), NOW()),
    (1, '좋은 진학 소식으로 알려줄게요!', FALSE, NOW(), NOW()),
    (2, '적절한 시기에 군대에 갈거에요.', FALSE, NOW(), NOW()),
    (2, '긍정적인 마음으로 준비하고있어요.', FALSE, NOW(), NOW()),
    (2, '적당한 시기가 오면 다녀올게요!', FALSE, NOW(), NOW()),
    (3, '학교 공부 열심히 하고 있어요!', FALSE, NOW(), NOW()),
    (3, '학점과 경험을 쌓고있어요.', FALSE, NOW(), NOW()),
    (3, '성적도 잘 챙기고 있어요!', FALSE, NOW(), NOW()),
    (4, '삶을 즐기며 집중해요!', FALSE, NOW(), NOW()),
    (4, '놀 때와 열심히 할 때의 균형!', FALSE, NOW(), NOW()),
    (4, '잘 쉬어야 더 잘해요!', FALSE, NOW(), NOW()),
    (5, '모의고사 성적 계속 성장 중!', FALSE, NOW(), NOW()),
    (5, '매번 조금씩 발전해요.', FALSE, NOW(), NOW()),
    (5, '과정에 집중하고 있어요.', FALSE, NOW(), NOW()),
    (6, '제 꿈을 찾아가는 중이에요!', FALSE, NOW(), NOW()),
    (6, '제 길을 찾으려 해요.', FALSE, NOW(), NOW()),
    (6, '제 속도로 잘 가고 있어요.', FALSE, NOW(), NOW()),
    (7, '여기서 많이 배우고있어요!', FALSE, NOW(), NOW()),
    (7, '제 선택에 만족해요.', FALSE, NOW(), NOW()),
    (7, '경험으로 성장 중!', FALSE, NOW(), NOW()),
    (8, '제 방식대로 열심히 해요!', FALSE, NOW(), NOW()),
    (8, '각자의 속도가 있어요.', FALSE, NOW(), NOW()),
    (8, '저만의 성취를 위해 노력중!', FALSE, NOW(), NOW()),
    (9, '사람은 계속 변해요.', FALSE, NOW(), NOW()),
    (9, '지금도 성장 중이에요.', FALSE, NOW(), NOW()),
    (9, '다른 방식으로 빛나요.', FALSE, NOW(), NOW()),
    (10, '지금 모습도 만족해요!', FALSE, NOW(), NOW()),
    (10, '자신을 사랑하는 중!', FALSE, NOW(), NOW()),
    (10, '내면의 아름다움이 중요해요.', FALSE, NOW(), NOW()),
    (11, '변화를 긍정적으로 받아요.', FALSE, NOW(), NOW()),
    (11, '경험과 지혜가 중요해요.', FALSE, NOW(), NOW()),
    (11, '성숙해지고 있어요.', FALSE, NOW(), NOW()),
    (12, '키보다 자신감이 중요!', FALSE, NOW(), NOW()),
    (12, '저만의 매력이 있어요.', FALSE, NOW(), NOW()),
    (12, '자신감이 중요해요.', FALSE, NOW(), NOW()),
    (13, '진정한 아름다움은 자신감!', FALSE, NOW(), NOW()),
    (13, '제 스타일을 찾아가고 있어요.', FALSE, NOW(), NOW()),
    (13, '화장은 제 표현수단이에요.', FALSE, NOW(), NOW()),
    (14, '배우는 것에 집중하고있어요.', FALSE, NOW(), NOW()),
    (14, '편안함과 실용성 중시!', FALSE, NOW(), NOW()),
    (14, '자연스러운 모습을 좋아해요.', FALSE, NOW(), NOW()),
    (15, '피부 건강하게 관리 중!', FALSE, NOW(), NOW()),
    (15, '피부 관리에 신경쓰고있어요.', FALSE, NOW(), NOW()),
    (15, '내면의 건강이 중요해요.', FALSE, NOW(), NOW()),
    (16, '현재 모습에 만족해요!', FALSE, NOW(), NOW()),
    (16, '건강 관리 중이에요!', FALSE, NOW(), NOW()),
    (16, '자신감이 아름다움을 줘요.', FALSE, NOW(), NOW()),
    (17, '건강한 생활 하고 있어요!', FALSE, NOW(), NOW()),
    (17, '현재 상태에 만족해요.', FALSE, NOW(), NOW()),
    (17, '자신감 가지고 있어요.', FALSE, NOW(), NOW()),
    (18, '문신은 제 이야기에요.', FALSE, NOW(), NOW()),
    (18, '타투는 제 표현수단이에요.', FALSE, NOW(), NOW()),
    (18, '요즘 타투는 꽤나 많이해요!', FALSE, NOW(), NOW()),
    (19, '금연을 고려 중이에요!', FALSE, NOW(), NOW()),
    (19, '건강한 삶을 위해 노력중!', FALSE, NOW(), NOW()),
    (19, '건강이 최우선이라고 생각해요.', FALSE, NOW(), NOW()),
    (20, '자신을 발전시키는 중!', FALSE, NOW(), NOW()),
    (20, '저만의 다른 목표들이 있어요.', FALSE, NOW(), NOW()),
    (20, '기다릴 준비가 되어 있어요.', FALSE, NOW(), NOW()),
    (21, '긍정적으로 기다리고있어요.', FALSE, NOW(), NOW()),
    (21, '자기 발전에 집중해요.', FALSE, NOW(), NOW()),
    (21, '제 길을 가고 있어요.', FALSE, NOW(), NOW()),
    (22, '열정적으로 일하는중!', FALSE, NOW(), NOW()),
    (22, '그(녀)의 자랑스러운 직장이에요.', FALSE, NOW(), NOW()),
    (22, '그(녀)의 열정을 응원해요.', FALSE, NOW(), NOW()),
    (23, '서로를 더 알아가요.', FALSE, NOW(), NOW()),
    (23, '미래에 대해 생각 중!', FALSE, NOW(), NOW()),
    (23, '결혼은 신중히 생각해요.', FALSE, NOW(), NOW()),
    (24, '적절한 시기에 소개할게요.', FALSE, NOW(), NOW()),
    (24, '준비됐을 때 소개할게요.', FALSE, NOW(), NOW()),
    (24, '서로 확신할 때 소개해요.', FALSE, NOW(), NOW()),
    (25, '부모님은 멋진 분들이에요.', FALSE, NOW(), NOW()),
    (25, '부모님을 소개하고 싶어요.', FALSE, NOW(), NOW()),
    (25, '부모님은 열심히 하세요.', FALSE, NOW(), NOW()),
    (26, '저희는 서로를 잘 보살펴요.', FALSE, NOW(), NOW()),
    (26, '저희는 최선을 다하는 관계예요.', FALSE, NOW(), NOW()),
    (26, '저희는 서로의 가치를 인정해요.', FALSE, NOW(), NOW()),
    (27, '적절한 시기를 기다리고있어요.', FALSE, NOW(), NOW()),
    (27, '개인적인 성장에 집중하는중!', FALSE, NOW(), NOW()),
    (27, '결혼은 천천히 결정하려구요.', FALSE, NOW(), NOW()),
    (28, '혼수는 함께 결정할거에요.', FALSE, NOW(), NOW()),
    (28, '혼수 아직 준비중이에요.', FALSE, NOW(), NOW()),
    (28, '필요한 것들로 준비할게요.', FALSE, NOW(), NOW()),
    (29, '신혼집 신중히 고려 중!', FALSE, NOW(), NOW()),
    (29, '알맞는 조건을 찾아 결정하려구요.', FALSE, NOW(), NOW()),
    (29, '신혼집은 점차 키워나가려구요', FALSE, NOW(), NOW()),
    (30, '제 길에 집중하고 있어요.', FALSE, NOW(), NOW()),
    (30, '자연스러운 만남을 선호해요.', FALSE, NOW(), NOW()),
    (30, '맞선은 조금 기다릴게요.', FALSE, NOW(), NOW()),
    (31, '노후에도 취미를 즐길거에요.', FALSE, NOW(), NOW()),
    (31, '다양한 관계를 유지할거에요.', FALSE, NOW(), NOW()),
    (31, '의미 있는 삶을 준비할거에요.', FALSE, NOW(), NOW()),
    (32, '좋은 친구를 만나고 있어요.', FALSE, NOW(), NOW()),
    (32, '저보다 조금 아깝긴하지만요.', FALSE, NOW(), NOW()),
    (32, '어울리려고 노력중이에요.', FALSE, NOW(), NOW()),
    (33, '제 속도로 열심히 할게요!', FALSE, NOW(), NOW()),
    (33, '친구 성공도 기뻐해요.', FALSE, NOW(), NOW()),
    (33, '제 업무에 만족하며 배워요.', FALSE, NOW(), NOW()),
    (34, '기술 배우는 것 열려 있어요.', FALSE, NOW(), NOW()),
    (34, '기술 습득도 중요해요.', FALSE, NOW(), NOW()),
    (34, '새로운 것을 배우려고 해요.', FALSE, NOW(), NOW()),
    (35, '알바도 고려 중이에요!', FALSE, NOW(), NOW()),
    (35, '알바로 경험 쌓고 싶어요.', FALSE, NOW(), NOW()),
    (35, '사회 경험을 쌓고 있어요.', FALSE, NOW(), NOW()),
    (36, '모든 경험이 성장에 도움돼요!', FALSE, NOW(), NOW()),
    (36, '배우고 성장하는 중이에요.', FALSE, NOW(), NOW()),
    (36, '폭넓은 배움을 추구해요.', FALSE, NOW(), NOW()),
    (37, '취업 준비 천천히 하려구요.', FALSE, NOW(), NOW()),
    (37, '준비된 미래를 위해 노력중!', FALSE, NOW(), NOW()),
    (37, '제대로 준비하고 싶어요.', FALSE, NOW(), NOW()),
    (38, '친구들도 잘하고 있어요.', FALSE, NOW(), NOW()),
    (38, '제 목표를 향해 달려가려구요!', FALSE, NOW(), NOW()),
    (38, '긍정적인 변화 기대중!', FALSE, NOW(), NOW()),
    (39, '전공으로 가능성 탐색 중!', FALSE, NOW(), NOW()),
    (39, '최선의 길을 찾으려 해요.', FALSE, NOW(), NOW()),
    (39, '전공을 통해 많이 배워요.', FALSE, NOW(), NOW()),
    (40, '좋은 기회를 찾으려구요.', FALSE, NOW(), NOW()),
    (40, '선택의 폭을 넓히려고 해요.', FALSE, NOW(), NOW()),
    (40, '긍정적인 결과를 위해 준비중!', FALSE, NOW(), NOW()),
    (41, '제가 하고 싶은 일을 찾으려구요.', FALSE, NOW(), NOW()),
    (41, '여러 요소들을 고려하려구요.', FALSE, NOW(), NOW()),
    (41, '배움과 성장이 우선이에요.', FALSE, NOW(), NOW()),
    (42, '전 여기서 많이 배우고 있어요.', FALSE, NOW(), NOW()),
    (42, '현재 위치에서 최선을 다하는중!', FALSE, NOW(), NOW()),
    (42, '경력에 도움이 되는 기회 찾는중!', FALSE, NOW(), NOW()),
    (43, '졸업 계획 잘 짜여 있어요.', FALSE, NOW(), NOW()),
    (43, '학업에 집중하고 있어요.', FALSE, NOW(), NOW()),
    (43, '여러 경험을 하고 있어요.', FALSE, NOW(), NOW()),
    (44, '회사의 미래와 성장을 고려했어요.', FALSE, NOW(), NOW()),
    (44, '함께 성장하고 싶어요.', FALSE, NOW(), NOW()),
    (44, '경력 목표와 회사 비전 일치 탐색 중!', FALSE, NOW(), NOW()),
    (45, '제 길을 찾아 나아가려구요!', FALSE, NOW(), NOW()),
    (45, '자신에 맞는 길 찾기!', FALSE, NOW(), NOW()),
    (45, '제 분야에서 최선을 다해요.', FALSE, NOW(), NOW()),
    (46, '재정적 독립 준비중!', FALSE, NOW(), NOW()),
    (46, '경제적 독립을 위한 준비중!', FALSE, NOW(), NOW()),
    (46, '경제적 독립 목표로 준비중!', FALSE, NOW(), NOW()),
    (47, '가족에게 감사하며 살고있어요.', FALSE, NOW(), NOW()),
    (47, '가족에게 좋은 결과로 보답할게요.', FALSE, NOW(), NOW()),
    (47, '가족을 위해 최선을 다해요.', FALSE, NOW(), NOW()),
    (48, '가족을 따뜻하게 대해요.', FALSE, NOW(), NOW()),
    (48, '가족과의 시간 소중히 해요.', FALSE, NOW(), NOW()),
    (48, '가족 관계를 돈독히 해요.', FALSE, NOW(), NOW()),
    (49, '가족을 위해 책임감 가지고 있어요.', FALSE, NOW(), NOW()),
    (49, '가족 보살피는 것 중요해요.', FALSE, NOW(), NOW()),
    (49, '가족을 위한 지원군 되려고 해요.', FALSE, NOW(), NOW()),
    (50, '최선을 다해서 잘 될 거예요.', FALSE, NOW(), NOW()),
    (50, '긍정적인 결과로 보답할게요.', FALSE, NOW(), NOW()),
    (50, '기대에 부응하려고 해요.', FALSE, NOW(), NOW()),
    (51, '첫째로서 역할 충실히 하고있어요.', FALSE, NOW(), NOW()),
    (51, '가족을 위해 최선을 다해요.', FALSE, NOW(), NOW()),
    (51, '가족을 잘 돌보려고 해요.', FALSE, NOW(), NOW()),
    (52, '집안일에 최선을 다하고있어요.', FALSE, NOW(), NOW()),
    (52, '가족 구성원으로 집안일 참여!', FALSE, NOW(), NOW()),
    (52, '집안일 통해 도움 되고 싶어요.', FALSE, NOW(), NOW()),
    (53, '가족과 더 자주 연락할게요.', FALSE, NOW(), NOW()),
    (53, '더 자주 연락드리겠습니다.', FALSE, NOW(), NOW()),
    (53, '가족과의 소통 강화할게요.', FALSE, NOW(), NOW()),
    (54, '경제적으로 효도하겠습니다.', FALSE, NOW(), NOW()),
    (54, '가족을 부양하려고 노력해볼게요.', FALSE, NOW(), NOW()),
    (54, '경제적 성장을 위한 노력중이에요.', FALSE, NOW(), NOW()),
    (55, '부모님 모시는 일도 최선 다할게요.', FALSE, NOW(), NOW()),
    (55, '부모님께 꾸준히 효도는 할게요.', FALSE, NOW(), NOW()),
    (55, '다방면으로 방법을 고민중이에요.', FALSE, NOW(), NOW()),
    (56, '건강한 식생활을 위해 노력중!', FALSE, NOW(), NOW()),
    (56, '해먹으려고 노력하고 있어요.', FALSE, NOW(), NOW()),
    (56, '새로운 요리 배우며 요리해요.', FALSE, NOW(), NOW()),
    (57, '최선을 다해 준비 중이에요.', FALSE, NOW(), NOW()),
    (57, '서로 도우며 결혼 준비중이에요.', FALSE, NOW(), NOW()),
    (57, '독립적으로 준비하고 있어요.', FALSE, NOW(), NOW()),
    (58, '가족은 언제든 환영이에요.', FALSE, NOW(), NOW()),
    (58, '잘 모시기 위해 준비하고 싶어요.', FALSE, NOW(), NOW()),
    (58, '미리 알려주시면 좋아요.', FALSE, NOW(), NOW()),
    (59, '엄마를 돕는 건 당연해요.', FALSE, NOW(), NOW()),
    (59, '엄마를 도울 일 찾아볼게요.', FALSE, NOW(), NOW()),
    (59, '먼저 돕도록 노력할게요.', FALSE, NOW(), NOW()),
    (60, '적절한 시기에 알려드릴게요!', FALSE, NOW(), NOW()),
    (60, '아이는 큰 결정이에요.', FALSE, NOW(), NOW()),
    (60, '준비되면 알려드릴게요!', FALSE, NOW(), NOW()),
    (61, '아들, 딸 모두 소중해요.', FALSE, NOW(), NOW()),
    (61, '딸 같은 아들도 있는걸요!', FALSE, NOW(), NOW()),
    (61, '성별 상관없이 잘 키울게요.', FALSE, NOW(), NOW()),
    (62, '존재만으로도 든든한걸요!', FALSE, NOW(), NOW()),
    (62, '아들 같은 딸도 있는걸요!', FALSE, NOW(), NOW()),
    (62, '성별 상관없이 잘 키울게요.', FALSE, NOW(), NOW()),
    (63, '다양한 선택 존중받고 싶어요.', FALSE, NOW(), NOW()),
    (63, '제 인생 신중히 결정할게요.', FALSE, NOW(), NOW()),
    (63, '아이 갖는 건 개인 선택이에요.', FALSE, NOW(), NOW()),
    (64, '요즘에는 신중히들 결정하는걸요!', FALSE, NOW(), NOW()),
    (64, '저희 세대에 맞게 준비하려구요.', FALSE, NOW(), NOW()),
    (64, '육아가 생각보다 어렵다더라구요.', FALSE, NOW(), NOW()),
    (65, '독립을 위한 준비 중이에요!', FALSE, NOW(), NOW()),
    (65, '독립하는 건 큰 결정이죠.', FALSE, NOW(), NOW()),
    (65, '준비되면 독립할 예정이에요.', FALSE, NOW(), NOW()),
    (66, '코인 투자는 신중히 하려구요.', FALSE, NOW(), NOW()),
    (66, '다방면으로 노력하고 있어요!', FALSE, NOW(), NOW()),
    (66, '다양한 투자 옵션 고려하고 있어요.', FALSE, NOW(), NOW()),
    (67, '꾸준히 모으고 있어요.', FALSE, NOW(), NOW()),
    (67, '경제적 안정을 목표로 하고있어요.', FALSE, NOW(), NOW()),
    (67, '균형 잡힌 재정 관리 중이에요!', FALSE, NOW(), NOW()),
    (68, '반려 동물도 가족인걸요.', FALSE, NOW(), NOW()),
    (68, '저는 모든 해주고 싶은걸요.', FALSE, NOW(), NOW()),
    (68, '반려 동물이 제 삶의 기쁨이에요.', FALSE, NOW(), NOW()),
    (69, '신념에 따라 성실히 살고있어요.', FALSE, NOW(), NOW()),
    (69, '제게 맞는 신앙 생활을 하려구요.', FALSE, NOW(), NOW()),
    (69, '다른 신경써야 할 일이 많아서요!', FALSE, NOW(), NOW()),
    (70, '소중한 친구들이 함께하고 있어요.', FALSE, NOW(), NOW()),
    (70, '고민을 나누는 친구가 있어요.', FALSE, NOW(), NOW()),
    (70, '친구들과 친하게 지내고 있어요.', FALSE, NOW(), NOW()),
    (71, '취미로 활력 얻어요!', FALSE, NOW(), NOW()),
    (71, '취미 생활로 에너지 얻어요.', FALSE, NOW(), NOW()),
    (71, '아이돌 응원은 삶의 즐거움!', FALSE, NOW(), NOW()),
    (72, '개성과 스타일 자유롭게!', FALSE, NOW(), NOW()),
    (72, '이러한 모습도 좋게 봐주세요!', FALSE, NOW(), NOW()),
    (72, '자신감 있게 행동하는 거죠.', FALSE, NOW(), NOW()),
    (73, '게임으로 스트레스 해소해요.', FALSE, NOW(), NOW()),
    (73, '게임 시간 줄여볼게요!', FALSE, NOW(), NOW()),
    (73, '게임 즐기며 새로운 경험중!', FALSE, NOW(), NOW()),
    (74, '사용 시간 조절 중이에요.', FALSE, NOW(), NOW()),
    (74, '적당히 하도록 노력할게요.', FALSE, NOW(), NOW()),
    (74, '더 많은 야외 활동 할게요.', FALSE, NOW(), NOW()),
    (75, '재테크에 신중하게 접근하려구요.', FALSE, NOW(), NOW()),
    (75, '장기적 관점으로 접근해보려구요.', FALSE, NOW(), NOW()),
    (75, '리스크 관리 중요해요.', FALSE, NOW(), NOW()),
    (76, '부동산 투자 신중히 해보려구요.', FALSE, NOW(), NOW()),
    (76, '다른 기회도 많을거에요.', FALSE, NOW(), NOW()),
    (76, '장기적 관점으로 접근해보려구요.', FALSE, NOW(), NOW());