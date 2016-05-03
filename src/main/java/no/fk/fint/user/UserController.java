package no.fk.fint.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User")
@RestController
public class UserController {

    @ApiOperation("Get user info")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@RequestHeader(value = "x-org-id", required = false) String orgId) {
        if (orgId == null) {
            return new User("");
        } else {
            switch (orgId) {
                case "hfk.no":
                    return Users.HFK.get();
                case "rogfk.no":
                    return Users.RFK.get();
                default:
                    return new User(orgId);
            }
        }
    }
}
