package com.ms.web.user.controller;

import com.ms.pojo.User;
import com.ms.pojo.UserSummary;
import com.ms.service.IUserService;
import com.ms.service.IUserSummaryService;
import com.ms.service.ImageHandler;
import com.ms.web.common.Response;
import com.ms.web.user.response.QueryUserResponse;
import com.youguu.core.util.json.YouguuJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Date;

@SwaggerDefinition(
        tags = {
                @Tag(name = "v1.0", description = "用户API")
        }
)
@Api(value = "UserAction")
@Path(value = "/user")
@Controller("userAction")
public class UserAction {
    @Resource
    private IUserSummaryService userSummaryService;
    @Resource
    private IUserService userService;

    @GET
    @Path(value = "/getUserByOpenid")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "根据openid查询用户信息", notes = "", author = "更新于 2019-05-24")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "请求成功", response = QueryUserResponse.class),
            @ApiResponse(code = "0001", message = "用户不存在", response = QueryUserResponse.class)

    })
    public String getUserByOpenid(@ApiParam(value = "微信open_id", required = true) @QueryParam("openId") String openId) {

        try {
            User user = userService.getUserByOpenId(openId);

            if (user == null) {
                return YouguuJsonHelper.returnJSON("0001", "用户不存在");
            }

            UserSummary userSummary = userSummaryService.getUserSummary(user.getUid());

            QueryUserResponse response = new QueryUserResponse();
            response.setUid(user.getUid());
            response.setOpenId(user.getOpenId());
            response.setThirdId(user.getThirdId());
            response.setGameResult(user.getGameResult());
            response.setCommitStatus(user.getCommitStatus());
            response.setNickName(user.getNickName());
            response.setHeadPic(user.getHeadPic());

            if (null != userSummary) {
                response.setTimes(userSummary.getTimes());
                response.setUseTimes(userSummary.getUseTimes());
                response.setAllTimes(userSummary.getAllTimes());
                response.setGrandPrize(userSummary.getGrandPrize());
            }

            return YouguuJsonHelper.returnJSON("0000", "ok", response);
        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }
    }


    @POST
    @Path(value = "/addUser")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "添加用户", notes = "没有邀请openid或当前用户已成交", author = "更新于 2019-05-24")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "成功", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "参数错误", response = Response.class),
            @ApiResponse(code = "0001", message = "系统异常", response = Response.class)
    })
    public String addUser(@ApiParam(value = "第三方用户唯一标识", required = true) @FormParam("thirdId") String thirdId,
                          @ApiParam(value = "微信open_id", required = true) @FormParam("openId") String openId,
                          @ApiParam(value = "用户昵称", required = true) @FormParam("nickName") String nickName,
                          @ApiParam(value = "用户头像URL") @FormParam("headPic") String headPic,
                          @ApiParam(value = "成交状态：0-未成交；1-已成交") @FormParam("status") int status) {

        try {
            User user = new User();
            user.setThirdId(thirdId);
            user.setOpenId(openId);
            user.setGameResult(0);//默认未完成
            user.setCommitStatus(status);//默认未成交
            user.setNickName(nickName);
            user.setHeadPic(headPic);
            user.setcTime(new Date());

            int result = userService.addUser(user);
            if (result > 0) {
                return YouguuJsonHelper.returnJSON("0000", "成功");

            }
            return YouguuJsonHelper.returnJSON("0011", "保存用户信息失败");

        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }

    }

    @POST
    @Path(value = "/addUserWithInvite")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "添加并绑定用户", notes = "有邀请openid并且当前用户没有成交", author = "更新于 2019-05-24")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "成功", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "参数错误", response = Response.class),
            @ApiResponse(code = "0001", message = "系统异常", response = Response.class)
    })
    public String addUserWithInvite(@ApiParam(value = "第三方用户唯一标识", required = true) @FormParam("thirdId") String thirdId,
                                    @ApiParam(value = "微信open_id（被邀请人）", required = true) @FormParam("openId") String openId,
                                    @ApiParam(value = "微信open_id（邀请人）", required = true) @FormParam("inviteOpenId") String inviteOpenId,
                                    @ApiParam(value = "用户昵称", required = true) @FormParam("nickName") String nickName,
                                    @ApiParam(value = "用户头像URL") @FormParam("headPic") String headPic,
                                    @ApiParam(value = "成交状态：0-未成交；1-已成交") @FormParam("status") int status) {

        try {
            User user = new User();
            user.setThirdId(thirdId);
            user.setOpenId(openId);
            user.setGameResult(0);//默认未完成
            user.setCommitStatus(status);//默认未成交
            user.setNickName(nickName);
            user.setHeadPic(headPic);
            user.setcTime(new Date());

            User inviteUser = userService.getUserByOpenId(inviteOpenId);

            if (null == inviteUser) {
                return YouguuJsonHelper.returnJSON("0013", "邀请人不存在");
            }
            int result = userService.addUser(user, inviteUser.getUid());
            if (result > 0) {
                return YouguuJsonHelper.returnJSON("0000", "成功");

            }
            return YouguuJsonHelper.returnJSON("0011", "保存用户信息失败");

        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }

    }

    @POST
    @Path(value = "/genPlaybill")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "生成海报", notes = "生成带有邀请二维码和用户头像的个性海报", author = "更新于 2019-05-24")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "成功", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "参数错误", response = Response.class),
            @ApiResponse(code = "0001", message = "系统异常", response = Response.class)
    })
    public String genPlaybill(@ApiParam(value = "用户ID", required = true) @FormParam("uid") int uid) {

        try {
            User user = userService.getUser(uid);
            if (null == user) {
                return YouguuJsonHelper.returnJSON("0011", "用户不存在");
            }
            String headUrl = ImageHandler.genPlaybill(uid, user.getNickName(),user.getOpenId(),user.getThirdId(),user.getHeadPic());

            return YouguuJsonHelper.returnJSON("0000", "ok", headUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }

    }

    @POST
    @Path(value = "/finishGame")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "修改用户游戏状态", notes = "", author = "更新于 2019-05-24")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "成功", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "参数错误", response = Response.class),
            @ApiResponse(code = "0001", message = "系统异常", response = Response.class)
    })
    public String finishGame(@ApiParam(value = "用户ID", required = true) @FormParam("uid") int uid) {

        try {
            int result = userService.finishGame(uid);
            if (result > 0) {
                return YouguuJsonHelper.returnJSON("0000", "成功");

            }
            return YouguuJsonHelper.returnJSON("0011", "修改用户游戏状态失败");
        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }

    }

    @POST
    @Path(value = "/lottery")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "抽奖", notes = "", author = "更新于 2019-05-26")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "中奖了", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0003", message = "没有抽奖机会", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0004", message = "未中奖", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "参数错误", response = Response.class),
            @ApiResponse(code = "0012", message = "网络加速中", response = Response.class)
    })
    public String lottery(@ApiParam(value = "用户ID", required = true) @FormParam("uid") int uid) {

        try {
            //-1:没有抽奖机会；0:未中奖；1:中奖了
            int result = userSummaryService.lottery(uid);
            if (result < 0) {
                return YouguuJsonHelper.returnJSON("0003", "没有抽奖机会啦");
            }
            if (result == 0) {
                return YouguuJsonHelper.returnJSON("0004", "未中奖");
            }

            return YouguuJsonHelper.returnJSON("0000", "中奖了");
        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }
    }

    @POST
    @Path(value = "/updateUserCommit")
    @Produces("text/html;charset=UTF-8")
    @ApiOperation(value = "修改用户成交状态", notes = "", author = "更新于 2019-05-26")
    @ApiResponses(value = {
            @ApiResponse(code = "0000", message = "成功", response = com.ms.web.common.Response.class),
            @ApiResponse(code = "0002", message = "修改成交状态失败", response = Response.class),
            @ApiResponse(code = "0012", message = "网络加速中", response = Response.class)
    })
    public String updateUserCommit(@ApiParam(value = "用户ID", required = true) @FormParam("uid") int uid) {

        try {
            int result = userService.updateUserCommit(uid);
            if (result > 0) {
                return YouguuJsonHelper.returnJSON("0000", "修改成功");
            }

            return YouguuJsonHelper.returnJSON("0002", "修改成交状态失败");
        } catch (Exception e) {
            e.printStackTrace();
            return YouguuJsonHelper.returnJSON("0012", "网络加速中");
        }
    }
}
