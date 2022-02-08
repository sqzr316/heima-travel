package cn.itcast.travel.dao;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteListDao {
    PageBean<Route> findMyfavoriteList(int uid, int currentPage);
}
