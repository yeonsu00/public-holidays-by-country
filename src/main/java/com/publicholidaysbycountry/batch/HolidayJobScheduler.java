package com.publicholidaysbycountry.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job holidaySyncJob;

    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(holidaySyncJob, jobParameters);
    }
}
