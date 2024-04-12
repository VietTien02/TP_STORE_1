    package com.pts.controller.site;

    import com.pts.dao.CourseCodeDAO;
    import com.pts.dao.OrderDAO;
    import com.pts.entity.Course;
    import com.pts.entity.CourseCode;
    import com.pts.entity.Order;
    import org.springframework.beans.factory.annotation.Autowired;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.PostMapping;

    import org.springframework.web.bind.annotation.RequestParam;


    import javax.servlet.http.HttpServletRequest;


    @Controller
    public class CodeCourseController {
        @Autowired
        CourseCodeDAO courseCodeDAO;
        @Autowired
        OrderDAO orderDAO;

        @PostMapping("/kichhoatkhoahoc")
        public ResponseEntity<Object> index(Model m, @RequestParam("code")String code, HttpServletRequest request){

            String username=request.getRemoteUser();//lay username
            CourseCode Code =courseCodeDAO.laymacode(code);//thuc hien truy van kiem tra ma code nguoi dung nhap

            //neu code sai
            if(Code == null) {
                return new ResponseEntity<>("invalid", HttpStatus.BAD_REQUEST);
            }
            int sol=Code.getQuantity();
            if(sol <=0){
                return new ResponseEntity<>("invalid1", HttpStatus.BAD_REQUEST);
            }
            //kiem tra code co duoc su dung chua
            Order order = new Order();
            int id = Code.getCourseCode().getTps_id();
            Order orderExisted = orderDAO.kiemtracode(username, id);
            if(orderExisted != null) {
                return new ResponseEntity<>("used", HttpStatus.BAD_REQUEST);
            }
            Course course=Code.getCourseCode();
            int ca = Code.getCourseCode().getTps_id();
            order.setTps_username(username);
            order.setCourseor(course);
            order.setTps_statuscode(true);
            order.setPay("Code");
            Code.setQuantity(Code.getQuantity()-1);
            courseCodeDAO.save(Code);
            orderDAO.save(order);

            return new ResponseEntity<>("success", HttpStatus.OK);
        }

    }
