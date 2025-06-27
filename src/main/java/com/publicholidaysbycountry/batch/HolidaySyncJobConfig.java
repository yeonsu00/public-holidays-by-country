package com.publicholidaysbycountry.batch;

import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class HolidaySyncJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final HolidayItemReader reader;
    private final HolidayItemProcessor processor;
    private final HolidayItemWriter writer;

    @Bean
    public Job holidaySyncJob() {
        return new JobBuilder("holidaySyncJob", jobRepository)
                .start(holidaySyncStep())
                .build();
    }

    @Bean
    public Step holidaySyncStep() {
        return new StepBuilder("holidaySyncStep", jobRepository)
                .<HolidayDTO, Holiday>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
