package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteListDao;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FavoriteListDaoImpl implements FavoriteListDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public PageBean<Route> findMyfavoriteList(int uid, int currentPage) {
        // 查询有多少条喜爱记录
        String sql = "select * from tab_route where rid in(select rid from tab_favorite where uid = ?)";
        List<Route> myfavoriteList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid);
        int totalCount = myfavoriteList.size();

        // 查询总共有多少页
        int pageSize = 8; // 默认
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

        // 查询当前页数据
        int start = (currentPage - 1) * pageSize;
        String sql2 = "select * from tab_route where rid in(select rid from tab_favorite where uid = ?) limit ?, ?";
        List<Route> favoriteList = template.query(sql2, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
        PageBean<Route> routePageBean = new PageBean<>();
        routePageBean.setTotalCount(totalCount);
        routePageBean.setPageSize(pageSize);
        routePageBean.setTotalPage(totalPage);
        routePageBean.setList(favoriteList);
        routePageBean.setCurrentPage(currentPage);
        return routePageBean;
    }
}
