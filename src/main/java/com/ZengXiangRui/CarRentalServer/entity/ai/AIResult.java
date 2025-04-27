package com.ZengXiangRui.CarRentalServer.entity.ai;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
public class AIResult {
    String model;
    Date created_at;
    String response;
    boolean done;
    String done_reason;
    List<Integer> context;
    BigInteger total_duration;
    BigInteger load_duration;
    BigInteger prompt_eval_count;
    BigInteger prompt_eval_duration;
    BigInteger eval_count;
    BigInteger eval_duration;
}
