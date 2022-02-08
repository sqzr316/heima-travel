package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RouteServlet", value = "/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();


    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");


        int cid = 0;
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equalsIgnoreCase(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0; // 每页显示条数
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
        writeValue(pb, response);
        //4.将PageBean序列化为json返回
    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id参数
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json，写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取线路id
        String rid = request.getParameter("rid");
        // 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid; // 用户id
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }

        //3.调用FavoriteServce判断是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4.写回客户端
        writeValue(flag, response);
    }


    /**
     * 添加收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取rid
        String rid = request.getParameter("rid");
        //2.获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid; // 用户id
        if (user == null) {
            //用户未登录
            return;
        } else {
            uid = user.getUid();
        }
        //3.调用service进行添加
        favoriteService.add(rid, uid);
    }

    public void findMyfavoriteList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 返回一个pagebean
        // 获取当前页码
        String currentPageStr = request.getParameter("currentPage");
        int currentPage = 0;
        if (currentPageStr == null || currentPageStr.length() <= 0 || "null".equalsIgnoreCase(currentPageStr)) {
            // 未设置页码 默认1
            currentPage = 1;
        } else {
            currentPage = Integer.parseInt(currentPageStr);
        }

        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();
        PageBean<Route> myfavoriteList = favoriteService.findMyfavoriteList(uid, currentPage);
        writeValue(myfavoriteList, response);
    }


}

