package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.repository.CarRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

/**
 * @author tangwei - Toby
 * @date 2018-12-26
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarService {
    private Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;


    public Car findById(String id) {
        return carRepository.findById(id).orElse(null);
    }

    /**
     * 保存车辆信息
     *
     * @param car
     * @return
     */
    public Car saveOrUpdate(Car car) {
        if (null == car) {
            return null;
        }
        String id = car.getId();
        String clientId = car.getClientId();
        if (StringUtils.isEmpty(clientId)) {
            logger.error("保存车辆信息失败：数据中没有包含clientId。");
            return null;
        }

        if (StringUtils.isEmpty(id)) {
            //新增
            car.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            car.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //如果没有标识是否新车，则默认为新车（也就是“不是二手车”）
            if (null == car.getNewCar()) {
                car.setNewCar(false);
            }
            car.setDefaultCar(this.isFirstCar(clientId));
            car.setNeedInspectionRemind(false);
            car.setNeedInsuranceRemind(false);
        } else {
            //修改
            Optional<Car> optionalCar = carRepository.findById(id);
            if (!optionalCar.isPresent()) {
                return null;
            }
            Car source = optionalCar.get();
            UpdateTool.copyNullProperties(source, car);
        }
        //执行保存
        return carRepository.saveAndFlush(car);
    }

    /**
     * 判断车辆是否为某用户名下第一辆车，若“真”，返回true
     *
     * @param clientId
     * @return
     */
    private boolean isFirstCar(String clientId) {
        List<Car> carList = carRepository.findByClientIdAndDelStatus(clientId, Constants.DelStatus.NORMAL.isValue());
        if (null != carList) {
            return carList.isEmpty();
        }
        return true;
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject delete(String id) {
        try {
            int result = carRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }

    /**
     * 新增/修改车辆
     *
     * @param car
     * @return
     */

    public ResultJsonObject modify(Car car) {
        try {
            //验重
            if (this.checkRepeatName(car)) {
                return ResultJsonObject.getErrorResult(null, "该门店已包含名称为：“" + car.getLicensePlate() + "”的数据，不能重复添加。");
            }

            Car res = this.saveOrUpdate(car);
            if (null == res) {
                return ResultJsonObject.getErrorResult(null);
            }
            return ResultJsonObject.getDefaultResult(res);
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     *
     * @param car
     * @return
     */
    private boolean checkRepeatName(Car car) {
        List<Car> carList;
        if (null != car.getId()) {
            carList = carRepository.checkRepeatName(car.getId(), car.getLicensePlate(), car.getStoreId());
        } else {
            carList = carRepository.checkRepeatName(car.getLicensePlate(), car.getStoreId());
        }
        return carList.size() != 0;
    }


    /**
     * 根据车牌号和门店ID查询对应车辆信息
     *
     * @param licensePlate
     * @param storeId
     * @return
     */
    public Car findCarByLicensePlateAndStoreId(String licensePlate, String storeId) {
        if (StringUtils.isEmpty(licensePlate) || StringUtils.isEmpty(storeId)) {
            return null;
        }
        //车牌号全部转换成大写
        licensePlate = licensePlate.toUpperCase();
        return carRepository.findTopByLicensePlateAndStoreIdAndAndDelStatusOrderByCreateTimeDesc(licensePlate, storeId, Constants.DelStatus.NORMAL.isValue());
    }

    /**
     * 加载某个车主名下的所有车辆
     *
     * @param clientId
     * @return jsonResult
     */
    public ResultJsonObject listPersonalCars(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return ResultJsonObject.getErrorResult(null, "参数clientId为空值");
        }
        return ResultJsonObject.getDefaultResult(listClientCars(clientId));
    }

    /**
     * 加载某个车主名下的所有车辆
     *
     * @param clientId
     * @return list
     */
    public List<Car> listClientCars(String clientId) {
        return carRepository.findByClientIdAndDelStatus(clientId, Constants.DelStatus.NORMAL.isValue());
    }

    /**
     * @param source
     * @param client
     * @return
     */
    public Car copyNewObjectForOtherStore(Car source, Client client) {
        if (null == source || null == client) {
            return null;
        }

        Car newCar = new Car();
        newCar.setDelStatus(Constants.DelStatus.NORMAL.isValue());

        newCar.setStoreId(client.getStoreId());
        newCar.setClientId(client.getId());

        newCar.setDefaultCar(source.getDefaultCar());
        newCar.setNewCar(source.getNewCar());
        newCar.setNeedInspectionRemind(source.getNeedInspectionRemind());
        newCar.setNeedInsuranceRemind(source.getNeedInsuranceRemind());
        newCar.setCarBrand(source.getCarBrand());
        newCar.setCarMark(source.getCarMark());
        newCar.setCarType(source.getCarType());
        newCar.setMiles(source.getMiles());
        newCar.setLastMiles(source.getLastMiles());
        newCar.setLicensePlate(source.getLicensePlate());
        newCar.setLicenseDate(source.getLicenseDate());
        newCar.setInsuranceStartTime(source.getInsuranceStartTime());
        newCar.setInsuranceEndTime(source.getInsuranceEndTime());
        newCar.setInsuranceCompany(source.getInsuranceCompany());
        newCar.setInsuranceCity(source.getInsuranceCity());
        newCar.setInsuranceAmount(source.getInsuranceAmount());
        newCar.setFrameNumber(source.getFrameNumber());
        newCar.setEngineNumber(source.getEngineNumber());
        newCar.setDriveLicenseNumber(source.getDriveLicenseNumber());
        newCar.setDefaultDate(source.getDefaultDate());

        return newCar;
    }
}
