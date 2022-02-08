package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {

    /**
     * 根据rid和uid来查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    Favorite findByUidAndRid(int rid, int uid);

    int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param
     * @param uid
     */
    void add(int rid, int uid);
}
