package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author bystander
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}