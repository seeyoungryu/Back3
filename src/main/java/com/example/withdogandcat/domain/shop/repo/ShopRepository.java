package com.example.withdogandcat.domain.shop.repo;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom {

    /**
     * 주어진 사용자에 해당하는 Shop 목록을 찾습니다.
     * @param user 사용자
     * @return Shop 목록
     */
    List<Shop> findByUser(User user);

    /**
     * 주어진 ShopType에 해당하는 모든 Shop 목록을 찾습니다.
     * @param shopType ShopType
     * @return Shop 목록
     */
    List<Shop> findAllByShopType(ShopType shopType);

    /**
     * 주어진 사용자에 해당하는 Shop 개수를 셉니다.
     * @param user 사용자
     * @return Shop 개수
     */
    int countByUser(User user);

    /**
     * 주어진 이름과 주소에 해당하는 Shop을 찾습니다.
     * @param shopName 상점 이름
     * @param address 주소
     * @return Shop 객체
     */
    Shop findByShopNameAndAddress(String shopName, String address);

    /**
     * 주어진 키워드에 따라 Shop을 검색합니다.
     * @param keyword 검색 키워드
     * @return Shop 목록
     */
    List<Shop> findByShopNameContainingOrShopAddressContaining(String keyword);
}
