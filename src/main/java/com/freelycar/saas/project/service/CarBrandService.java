package com.freelycar.saas.project.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.project.entity.CarBrand;
import com.freelycar.saas.project.entity.CarModel;
import com.freelycar.saas.project.entity.CarType;
import com.freelycar.saas.project.repository.CarBrandRepository;
import com.freelycar.saas.project.repository.CarModelRepository;
import com.freelycar.saas.project.repository.CarTypeRepository;
import com.freelycar.saas.util.CarBrandJSONResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-04
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarBrandService {
    private Logger logger = LoggerFactory.getLogger(CarBrandService.class);

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private CarModelRepository carModelRepository;

    /**
     * 初始化车系车型数据
     */
    public void initCarBrand() {
        logger.info("清空车牌表、车系表数据……");
        carTypeRepository.deleteAll();
        carBrandRepository.deleteAll();
        carModelRepository.deleteAll();
        logger.info("清空车牌表、车系表数据完毕！");

        logger.info("开始执行车牌车系数据初始化方法……");
        String pinyinArrayString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String[] pinyinArray = pinyinArrayString.split("");

        String jsonStr = CarBrandJSONResolver.readCarJSON();
        JSONObject jsonObj = JSON.parseObject(jsonStr);

        //遍历拼音首字母
        for (String pinyin : pinyinArray) {
            JSONArray brandJSONArray = jsonObj.getJSONArray(pinyin);
            //遍历brand
            for (int i = 0; i < brandJSONArray.size(); i++) {
                JSONObject brandJSONObject = brandJSONArray.getJSONObject(i);
                String brand = brandJSONObject.getString("brand");

                //组装brand数据
                CarBrand carBrand = new CarBrand();
                carBrand.setPinyin(pinyin);
                carBrand.setBrand(brand);
                carBrand = carBrandRepository.save(carBrand);

                //保存结束，取得主键
                Integer carBrandId = carBrand.getId();

                JSONArray typesJSONArray = brandJSONObject.getJSONArray("types");
                //遍历type
                for (int j = 0; j < typesJSONArray.size(); j++) {
                    JSONObject typeJSONObject = typesJSONArray.getJSONObject(j);
                    String typeName = typeJSONObject.getString("type");

                    //组装type数据
                    CarType carType = new CarType();
                    carType.setCarBrandId(carBrandId);
                    carType.setType(typeName);
                    carType = carTypeRepository.save(carType);

                    //取得主键
                    Integer carTypeId = carType.getId();

                    List<CarModel> carModels = new ArrayList<>();
                    JSONArray modelsJSONArray = typeJSONObject.getJSONArray("models");
                    //遍历model
                    for (int k = 0; k < modelsJSONArray.size(); k++) {
                        JSONObject modelJSONObject = modelsJSONArray.getJSONObject(k);
                        String model = modelJSONObject.getString("model");

                        //组装model数据
                        CarModel carModel = new CarModel();
                        carModel.setCarTypeId(carTypeId);
                        carModel.setModel(model);

                        carModels.add(carModel);
                    }
                    carModelRepository.saveAll(carModels);
                }
            }
        }

        logger.info("车牌车系数据初始化方法执行完毕，请检查！");
    }

    /**
     * 返回所有车辆品牌数据
     *
     * @return CarBrand对象集合
     */
    public List<CarBrand> getAllCarBrand() {
        return carBrandRepository.findAll();
    }

    /**
     * 返回拼音首字母对应的所有车辆品牌
     *
     * @param pinyin 拼音首字母
     * @return CarBrand对象集合
     */
    public List<CarBrand> getCarBrandByPinyin(String pinyin) {
        return carBrandRepository.findAllByPinyin(pinyin);
    }

    /**
     * 获取品牌下的车系列表
     *
     * @param carBrandId
     * @return
     */
    public List<CarType> getCarTypeByCarBrandId(Integer carBrandId) {
        return carTypeRepository.findAllByCarBrandId(carBrandId);
    }

    /**
     * 获取车系下的具体型号列表
     *
     * @param carTypeId
     * @return
     */
    public List<CarModel> getCarModelByCarTypeId(Integer carTypeId) {
        return carModelRepository.findAllByCarTypeId(carTypeId);
    }

    /**
     * 根据关键字获取车系
     *
     * @param keyword
     * @return
     */
    public List<CarBrand> getCarBrandByKeyword(String keyword) {
        return carBrandRepository.findAllByBrandContaining(keyword);
    }

    /**
     * 查找热门品牌
     *
     * @return
     */
    public List<CarBrand> getHotCarBrand() {
        return carBrandRepository.findByHot(true);
    }

}
