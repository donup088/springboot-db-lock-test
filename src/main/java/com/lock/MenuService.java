package com.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public void noneOptimisiticeOrder(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
        menu.order();
    }

    @Transactional
    public void optimisticOrder(Long menuId) {
        Menu menu = menuRepository.findByIdWithOptimisticLock(menuId).orElseThrow(IllegalArgumentException::new);
//        menu.order();
    }

    @Transactional
    public void pessimisticReadOrder(Long menuId){
        Menu menu = menuRepository.findByIdWithPessimisticReadLock(menuId).orElseThrow(IllegalArgumentException::new);
        menu.order();
    }

    @Transactional
    public void pessimisticWriteOrder(Long menuId){
        Menu menu = menuRepository.findByIdWithPessimisticWriteLock(menuId).orElseThrow(IllegalArgumentException::new);
        menu.order();
    }

    public Menu getOne(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void deleteAll() {
        menuRepository.deleteAll();
    }
}
