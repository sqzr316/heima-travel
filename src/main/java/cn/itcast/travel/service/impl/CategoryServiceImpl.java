package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
//        System.out.println("jedis:" + jedis);
        //1.2可使用sortedset排序查询 有序集合的命令 jedis.zrange 获取，jedis.zadd 存入添加
//        Set<String> categorys = jedis.zrange("category", 0, -1);
//        System.out.println("category:" + categorys);
        //1.3查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);

        List<Category> cs = null;
        //2.判断查询结果是否为空
        if (categorys == null || categorys.size() == 0) {
            System.out.println("从数据库查询...");
            //3.如果为空，需要从数据库查询，再将数据存入redis
            cs = categoryDao.findAll();
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        } else {
            //4.不为空，直接返回
            System.out.println("从redis中查询");
            cs = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category c = new Category();
                c.setCname(tuple.getElement());
                c.setCid((int) tuple.getScore());
                cs.add(c);
            }
        }
        return cs;
    }
}
