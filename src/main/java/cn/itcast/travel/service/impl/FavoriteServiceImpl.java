package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.FavoriteListDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.FavoriteListDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    private FavoriteListDao favoriteListDao = new FavoriteListDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = favoriteDao.findByUidAndRid(Integer.parseInt(rid), uid);

        return favorite != null; //有值则为true，无值为false
    }


    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid), uid);
    }

    @Override
    public PageBean<Route> findMyfavoriteList(int uid, int currentPage) {
        return favoriteListDao.findMyfavoriteList(uid, currentPage);
    }
}
