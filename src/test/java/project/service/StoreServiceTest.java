package project.service;

import com.freelycar.saas.basic.BaseTest;
import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.repository.StoreRepository;
import com.freelycar.saas.project.service.StoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.*;

import java.util.ArrayList;

import static com.alibaba.druid.sql.ast.SQLPartitionValue.Operator.List;

/**
 * @author tangwei - Toby
 * @date 2018/11/29
 * @email toby911115@gmail.com
 */
public class StoreServiceTest extends BaseTest {
    @Autowired
    StoreService storeService;

    @Test
    public void findAll() {
//        Assert.assertSame(null,storeService.findAll(0));
        System.out.println(storeService.findAll(0));
    }
}
