package com.lock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select m from Menu m where m.id=:menuId")
    Optional<Menu> findByIdWithPessimisticReadLock(Long menuId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Menu m where m.id=:menuId")
    Optional<Menu> findByIdWithPessimisticWriteLock(Long menuId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select m from Menu m where m.id=:menuId")
    Optional<Menu> findByIdWithOptimisticLock(Long menuId);
}
