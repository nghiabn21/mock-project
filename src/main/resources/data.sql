insert into public.roles ("name") values('ROLE_ADMIN');
insert into public.roles ("name") values('ROLE_RECRUITER');
insert into public.roles ("name") values('ROLE_MANAGER');
insert into public.roles ("name") values('ROLE_INTERVIEWER');


INSERT INTO public.person
(created_by, created_date, last_modified_by, last_modified_date, address, dob, email, full_name, gender, phone_number)
VALUES(NULL, NULL, NULL, NULL, 'Hai Duong', '2000-12-02 00:00:00.000', 'ksss@gmail.com', 'Vu Tien Khoi', 1, '0987654321');
INSERT INTO public.person
(created_by, created_date, last_modified_by, last_modified_date, address, dob, email, full_name, gender, phone_number)
VALUES(NULL, NULL, NULL, NULL, 'Ha Noi', '2000-02-02 00:00:00.000', 'oiww@fmm.com', 'Pham Cong Khien', 1, '0987612345');
INSERT INTO public.person
(created_by, created_date, last_modified_by, last_modified_date, address, dob, email, full_name, gender, phone_number)
VALUES(NULL, NULL, NULL, NULL, 'Nam Dinh', '2000-01-01 00:00:00.000', 'ga@gmail.com', 'Nguyen Duc Viet', 1, '0926371233');
INSERT INTO public.person
(created_by, created_date, last_modified_by, last_modified_date, address, dob, email, full_name, gender, phone_number)
VALUES('anonymousUser', '2022-12-27 10:33:00.133', 'anonymousUser', '2022-12-27 10:33:00.133', 'Ha noi', '2000-02-12 00:00:00.000', 'nguyenviet112@gmail.com', 'Nguyễn Đức Việt', 1, '0901994297');


INSERT INTO public.users
(created_by, created_date, last_modified_by, last_modified_date, account, expiry_date, "password", reset_password_token, status, temporary_password, person_id, department, notes)
VALUES('anonymousUser', '2022-12-27 10:33:00.126', 'anonymousUser', '2022-12-27 10:33:00.126', 'VietND21', NULL, '$2a$10$wqeJPS2pt3Unk5Ei9iOX1ulj.7/FYG/x7AACQcF6F7DGC.cBNyDr.', NULL, 1, NULL, 4, 5, 'test create');
INSERT INTO public.users
(created_by, created_date, last_modified_by, last_modified_date, account, expiry_date, "password", reset_password_token, status, temporary_password, person_id, department, notes)
VALUES(NULL, '2022-12-06 15:39:49.539', NULL, '2022-12-06 15:39:49.539', 'khoivt2', NULL, '$2a$10$wqeJPS2pt3Unk5Ei9iOX1ulj.7/FYG/x7AACQcF6F7DGC.cBNyDr.', NULL, 1, NULL, 1, NULL, NULL);
INSERT INTO public.users
(created_by, created_date, last_modified_by, last_modified_date, account, expiry_date, "password", reset_password_token, status, temporary_password, person_id, department, notes)
VALUES(NULL, '2022-12-06 15:39:49.539', NULL, '2022-12-06 15:39:49.539', 'KhienPC', NULL, '$2a$10$wqeJPS2pt3Unk5Ei9iOX1ulj.7/FYG/x7AACQcF6F7DGC.cBNyDr.', NULL, 1, NULL, 2, NULL, NULL);
INSERT INTO public.users
(created_by, created_date, last_modified_by, last_modified_date, account, expiry_date, "password", reset_password_token, status, temporary_password, person_id, department, notes)
VALUES(NULL, '2022-12-06 15:39:49.539', NULL, '2022-12-06 15:39:49.539', 'VietND20', NULL, '$2a$10$wqeJPS2pt3Unk5Ei9iOX1ulj.7/FYG/x7AACQcF6F7DGC.cBNyDr.', NULL, 1, NULL, 3, NULL, NULL);


INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 1);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 3);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 4);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(2, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(3, 4);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(4, 3);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(4, 1);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(4, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(4, 4);


INSERT INTO public.candidate
(created_by, created_date, last_modified_by, last_modified_date, status, cv_attachment, highest_level, note, "position", year_of_experience, person_id, recruiter_id)
VALUES(NULL, NULL, NULL, NULL, 0, 'VietND', 1, 'sdd', NULL, 1, 2, 1);
INSERT INTO public.candidate
(created_by, created_date, last_modified_by, last_modified_date, status, cv_attachment, highest_level, note, "position", year_of_experience, person_id, recruiter_id)
VALUES(NULL, NULL, NULL, NULL, 0, 'VietND', 2, 'sss', NULL, 1, 3, 1);


INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-23 09:39:18.128', 'anonymousUser', '2022-12-23 09:39:18.128', 'Pham Van Bach', 'meeting.com', '32579', 0, '2022-12-23 17:20:00.000', 4, 'Quy Buu', '2022-12-23 17:21:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-27 11:18:41.906', 'anonymousUser', '2022-12-27 11:18:41.906', 'HD', 'meeting.com', 'Hung Yen - Hai Duong', 0, '2000-06-20 11:10:00.000', 4, 'thanh', '2000-06-20 12:10:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-27 11:19:06.842', 'anonymousUser', '2022-12-27 11:19:06.842', 'HD', 'meeting.com', 'Hung Yen - Hai Duong', 0, '2000-06-20 11:10:00.000', 4, 'thanh', '2000-06-20 12:10:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-13 11:22:25.431', 'anonymousUser', '2022-12-13 11:22:25.431', 'Pham Van Bach', 'meeting.com', 'Ứng viên vip', 0, '2022-12-13 17:07:00.000', 4, 'okeee', '2022-12-13 17:08:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-13 11:28:43.746', 'anonymousUser', '2022-12-13 11:28:43.746', 'Pham Van Bach', 'meeting.com', 'Ứng viên vip', 0, '2022-12-13 17:08:00.000', 4, 'okeee', '2022-12-13 17:09:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-13 11:35:45.322', 'anonymousUser', '2022-12-13 11:35:45.322', 'Pham Van Bach', 'meeting.com', 'Ứng viên vip', 0, '2022-12-13 17:11:00.000', 3, 'okeee', '2022-12-13 17:12:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-13 13:06:39.906', 'anonymousUser', '2022-12-13 13:06:39.906', 'Pham Van Bach', 'meeting.com', 'Ứng viên vip', 0, '2022-12-13 17:12:00.000', 3, 'okeee', '2022-12-13 17:13:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-27 11:20:24.059', 'anonymousUser', '2022-12-27 11:20:24.059', 'HD', 'meeting.com', 'Hung Yen - Hai Duong', 0, '2000-06-20 11:10:00.000', 1, 'thanh', '2000-06-20 12:10:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES(NULL, '2022-12-27 11:20:24.059', 'anonymousUser', '2022-12-27 11:31:47.461', 'Phạm Văn Bạch', 'meeting.com', 'Test xem dc không', 0, '2000-06-20 11:10:00.000', 1, 'thanh', '2000-06-20 12:10:00.000', 1, 1);
INSERT INTO public.schedule
(created_by, created_date, last_modified_by, last_modified_date, "location", meeting_id, notes, "result", schedule_from, status, schedule_title, schedule_to, candidate_id, recruiter_owner_id)
VALUES('anonymousUser', '2022-12-15 09:48:57.382', 'anonymousUser', '2022-12-15 09:48:57.382', 'Pham Van Bach', 'meeting.com', 'Ứng viên vip', 0, '2022-12-15 17:20:00.000', 4, 'Quy Buu', '2022-12-15 17:21:00.000', 1, 1);




INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(1, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(2, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(3, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(4, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(5, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(6, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(7, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(8, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(9, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(10, 1);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(10, 3);
INSERT INTO public.schedule_interviewers
(schedule_id, interviewer_id)
VALUES(10, 2);




-- create job
INSERT INTO public.job (job_id, description, end_date, status, job_title, salary_from, salary_to, start_date,
                        working_address)
VALUES (DEFAULT, 'over 1 yearn exp', '2022-12-30 13:37:10.000000', 0, 'Java spring', 1000, 2000,
        '2022-11-30 13:37:41.000000', 'Da Nang');

-- create skill
INSERT INTO public.skill ( name)
VALUES ('Java');
INSERT INTO public.skill ( name)
VALUES ('Html');

insert into public."level" (name)
values ('Fresher');
insert into public."level" (name)
values ('Junior');
insert into public."level" (name)
values ('Senior');

-- temporary
INSERT INTO public.job_skill (job_id, skill_id)
VALUES (1, 1);
INSERT INTO public.job_skill (job_id, skill_id)
VALUES (1, 2);

-- create benefit
INSERT INTO public.benefit (benefit_id, name)
VALUES (DEFAULT, 'Lunch');
INSERT INTO public.benefit (benefit_id, name)
VALUES (DEFAULT, 'Bonus Tet Holiday');

-- temporary
INSERT INTO public.job_benefit (job_id, benefit_id)
VALUES (1, 1);
INSERT INTO public.job_benefit (job_id, benefit_id)
VALUES (1, 2);


INSERT INTO public.job_level  (job_id, level_id)
VALUES (1, 1);
INSERT INTO public.job_level  (job_id, level_id)
VALUES (1, 2);
INSERT INTO public.job_level  (job_id, level_id)
VALUES (1, 3);

INSERT INTO public.offer
(created_by, created_date, last_modified_by, last_modified_date, basic_salary, contract_end, contract_start, contract_type, department, due_date, "level", note, offer_status, "position", manager_id, recruiter_owner_id, schedule_id)
VALUES('anonymousUser', '2022-12-16 13:51:14.427', 'anonymousUser', '2022-12-16 13:51:14.427', 10000000, '2023-12-20 12:50:00.000', '2022-12-20 12:00:00.000', 1, 3, '2022-12-20 11:59:00.000', 4, 'okkkkkk', 0, 3, 1, 1, 1);
INSERT INTO public.offer
(created_by, created_date, last_modified_by, last_modified_date, basic_salary, contract_end, contract_start, contract_type, department, due_date, "level", note, offer_status, "position", manager_id, recruiter_owner_id, schedule_id)
VALUES('anonymousUser', '2022-12-19 14:02:52.492', 'anonymousUser', '2022-12-19 14:02:52.492', 10000000, '2023-12-20 12:50:00.000', '2022-12-20 12:00:00.000', 1, 1, '2022-12-20 11:59:00.000', 1, 'okkkkkk', 0, 1, 1, 1, 2);
INSERT INTO public.offer
(created_by, created_date, last_modified_by, last_modified_date, basic_salary, contract_end, contract_start, contract_type, department, due_date, "level", note, offer_status, "position", manager_id, recruiter_owner_id, schedule_id)
VALUES('anonymousUser', '2022-12-21 10:59:12.395', 'anonymousUser', '2022-12-21 10:59:12.395', 10000000, '2023-12-21 12:50:00.000', '2022-12-21 12:00:00.000', 1, 1, '2022-12-21 11:59:00.000', 1, 'Verify Java Script code not be implemented in the text area or the result section on click on the Submit button. For example <script>alert(“123”)</script> an alert should not be shownVerify Java Script code not be implemented in the text area or the resul', 0, 1, 1, 1, 3);
INSERT INTO public.offer
(created_by, created_date, last_modified_by, last_modified_date, basic_salary, contract_end, contract_start, contract_type, department, due_date, "level", note, offer_status, "position", manager_id, recruiter_owner_id, schedule_id)
VALUES('anonymousUser', '2022-12-26 09:43:16.773', 'anonymousUser', '2022-12-26 09:43:16.773', 10000000, '2023-12-26 12:00:00.000', '2022-12-26 12:00:00.000', 1, 1, '2022-12-26 11:59:00.000', 1, 'Verify Java Script code not be implemented in the text area or the result section on click on the Submit butto', 0, 1, 1, 1, 4);
INSERT INTO public.offer
(created_by, created_date, last_modified_by, last_modified_date, basic_salary, contract_end, contract_start, contract_type, department, due_date, "level", note, offer_status, "position", manager_id, recruiter_owner_id, schedule_id)
VALUES('anonymousUser', '2022-12-16 13:50:52.942', 'anonymousUser', '2022-12-27 10:25:59.657', 10000000, '2023-12-30 12:50:00.000', '2022-12-30 12:00:00.000', 1, 2, '2022-12-28 00:00:00.000', 4, 'okkkkkk', 2, 1, 1, 1, 5);
