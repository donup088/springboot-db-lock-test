package com.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class MenuOptimisticTest {
    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;

    private static final String WATER_MELLON = "수박";
    private static final Integer WATER_MELLON_COUNT = 100;

    @BeforeEach
    public void setup() {
        menuService.deleteAll();
    }

    @Test
    public void optimisticNoneTest() throws InterruptedException {
        final int numberOfThreads = 3;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Menu menu = Menu.builder().name(WATER_MELLON).count(WATER_MELLON_COUNT).build();
        Menu createdMenu = menuService.create(menu);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                menuService.noneOptimisiticeOrder(createdMenu.getId());
                latch.countDown();
            });
        }

        Thread.sleep(500);
    }

    @Test
    public void optimisticTest() throws InterruptedException {
        final int numberOfThreads = 3;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Menu menu = Menu.builder().name(WATER_MELLON).count(WATER_MELLON_COUNT).build();
        Menu createdMenu = menuService.create(menu);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                menuService.optimisticOrder(createdMenu.getId());
                latch.countDown();
            });
        }

        Thread.sleep(500);
    }

    @Test
    public void pessimisticReadTest() throws InterruptedException {
        final int numberOfThreads = 3;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Menu menu = Menu.builder().name(WATER_MELLON).count(WATER_MELLON_COUNT).build();
        Menu createdMenu = menuService.create(menu);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                menuService.pessimisticReadOrder(createdMenu.getId());
                latch.countDown();
            });
        }

        Thread.sleep(500);
    }

    @Test
    public void pessimisticWriteTest() throws InterruptedException {
        final int numberOfThreads = 2;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Menu menu = Menu.builder().name(WATER_MELLON).count(WATER_MELLON_COUNT).build();
        Menu createdMenu = menuService.create(menu);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                long startTime = System.currentTimeMillis();
                menuService.pessimisticWriteOrder(createdMenu.getId());
                latch.countDown();
                log.info("time = {}", System.currentTimeMillis() - startTime);
            });
        }

        Thread.sleep(500);
    }
}
