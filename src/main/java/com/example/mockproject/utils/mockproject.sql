-- create job
INSERT INTO public.job (job_id, description, end_date, status, job_title, salary_from, salary_to, start_date,
                        working_address, level)
VALUES (DEFAULT, 'over 1 yearn exp', '2022-12-30 13:37:10.000000', 0, 'Java spring', 1000, 2000,
        '2022-11-30 13:37:41.000000', 'Da Nang', 'Fresher');

-- create skill
INSERT INTO public.skill ( name)
VALUES ('Java');
INSERT INTO public.skill ( name)
VALUES ('Html');

-- temporary
INSERT INTO public.job_skill (job_id, skill_id)
VALUES (1, 1);

-- create benefit
INSERT INTO public.benefit (benefit_id, name)
VALUES (DEFAULT, 'Lunch');

-- temporary
INSERT INTO public.job_benefit (job_id, benefit_id)
VALUES (1, 1);

-- create person
INSERT INTO public.person (person_id, address, dob, email, full_name, gender, phone_number)
VALUES (DEFAULT, 'Da Nang', '2022-11-30 13:42:51.000000', 'duchm2@gmail.com', 'Hoang Minh Duc', 1, '0123456788');

-- create users
INSERT INTO public.users (user_id, account, new_password, old_password, status, person_id, created_by, created_date,
                          last_modified_by, last_modified_date)
VALUES (DEFAULT, 'Duchm2', 'ha12$th', '1234', 1, 1, 1, '2022-11-30 13:47:07.000000', 1, '2022-11-30 13:47:16.000000');

-- create candidate
INSERT INTO public.candidate (candidate_id, created_by, created_date, last_modified_by, last_modified_date, status,
                              cv_attachment, highest_level, note, position, year_of_experience, person_id, recruiter_id)
VALUES (DEFAULT, 1, '2022-11-30 14:59:33.000000', 1, '2022-11-30 14:59:37.000000', 1, 'Cv Python', 1, 'note', 'Senior',
        null, null, null);

-- temporary
INSERT INTO public.candidate_skill (candidate_id, skill_id)
VALUES (1, 1);
