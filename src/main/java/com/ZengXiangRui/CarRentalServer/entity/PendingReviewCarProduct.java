package com.ZengXiangRui.CarRentalServer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pendingReviewCarProduct")
public class PendingReviewCarProduct extends CarProduct {
}
