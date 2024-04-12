package com.pts.service.impl;

import com.pts.dao.OrderDAO;
import com.pts.service.OrderService;
import com.pts.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDAO orderDAO;

    @Override
    public List<Order> myCrouse(String username) {
        return orderDAO.myCrouse(username);
    }
}
