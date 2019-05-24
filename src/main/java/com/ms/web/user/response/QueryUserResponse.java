package com.ms.web.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * describe:
 *
 * @author: shi_lei@suixingpay.com
 * @date: 2019/05/24 10:43:10:43
 * @version: V1.0
 * @review:
 */
@ApiModel
public class QueryUserResponse {
    @ApiModelProperty("用户ID")
    private int uid;

    @ApiModelProperty("微信openId")
    private String openId;

    @ApiModelProperty("第三方用户ID")
    private String thirdId;

    @ApiModelProperty("游戏状态：0-未完成；1-已完成")
    private int gameResult;

    @ApiModelProperty("成交状态：0-未成交；1-已成交")
    private int commitStatus;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String headPic;

    /**
     * 中奖信息
     */
    @ApiModelProperty("抽奖次数")
    private int times;

    @ApiModelProperty("使用的次数")
    private int useTimes;

    @ApiModelProperty("总获取多少次抽奖机会")
    private int allTimes;

    @ApiModelProperty("1-没有获得大奖；2-获得大奖")
    private int grandPrize;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public int getCommitStatus() {
        return commitStatus;
    }

    public void setCommitStatus(int commitStatus) {
        this.commitStatus = commitStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public int getAllTimes() {
        return allTimes;
    }

    public void setAllTimes(int allTimes) {
        this.allTimes = allTimes;
    }

    public int getGrandPrize() {
        return grandPrize;
    }

    public void setGrandPrize(int grandPrize) {
        this.grandPrize = grandPrize;
    }
}
