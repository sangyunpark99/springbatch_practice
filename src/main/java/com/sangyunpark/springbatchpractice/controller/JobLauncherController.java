package com.sangyunpark.springbatchpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final JobLauncher jobLauncher; // 배치 Job을 실행할 때 사용
    private final JobExplorer jobExplorer; // 실행된 Job의 이력/상태를 조회할 때 사용
    private final Job helloJob; // 실제 실행하고자 하는 배치 Job 객체
    private final Job customerFileJob;

    @GetMapping("/batch/hello")
    public ResponseEntity<String> runHelloJob() {

        // jobParameter는 Job 실행을 식별하고, 매번 실행할 때마다 전달할 값들을 담는 객체
        JobParameters jobParameter = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString()) // 파라미터로 datetime 넘겨주기
                .toJobParameters();

        JobExecution jobExecution = null;

        try {
            jobExecution = jobLauncher.run(helloJob,jobParameter); // jobLauncher를 통해서 실행을 하게 된다.
            return ResponseEntity.ok("Job 실행 완료 상태: " + jobExecution.getStatus());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job 실행 실패 : " + e.getMessage());
        }
    }
    @GetMapping("/batch/customer")
    public ResponseEntity<String> runCustomer() {

        // jobParameter는 Job 실행을 식별하고, 매번 실행할 때마다 전달할 값들을 담는 객체
        JobParameters jobParameter = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString()) // 파라미터로 datetime 넘겨주기
                .toJobParameters();

        JobExecution jobExecution = null;

        try {
            jobExecution = jobLauncher.run(customerFileJob,jobParameter); // jobLauncher를 통해서 실행을 하게 된다.
            return ResponseEntity.ok("Job 실행 완료 상태: " + jobExecution.getStatus());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job 실행 실패 : " + e.getMessage());
        }
    }

}

