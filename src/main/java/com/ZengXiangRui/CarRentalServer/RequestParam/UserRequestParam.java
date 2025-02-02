package com.ZengXiangRui.CarRentalServer.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestParam {
    public String username;
    public String avatarUrl;
}
