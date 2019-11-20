package io.github.jokoframework.myproject.web.controller;

import io.github.jokoframework.myproject.basic.dto.CountryDTO;
import io.github.jokoframework.myproject.basic.service.CountryService;
import io.github.jokoframework.myproject.basic.service.DesktopService;
import io.github.jokoframework.myproject.constants.ApiPaths;
import io.github.jokoframework.myproject.web.response.BaseResponseDTO;
import io.github.jokoframework.security.controller.SecurityConstants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DesktopController {

    @Autowired
    private DesktopService desktopService;

    @ApiOperation(value = "Bloquea un escritorio",
            notes = "Ejecuta el comando configurado para bloquear la pantalla")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bloqueo exitoso"),
    })
    @RequestMapping(value = ApiPaths.LOCK_DESKTOP, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = SecurityConstants.AUTH_HEADER_NAME, dataType = "String",
            paramType = "header", required = false, value = "Access Token")
    public BaseResponseDTO lock() {
        return desktopService.lockDesktop();
    }

    @ApiOperation(value = "Desbloquea un escritorio",
            notes = "Ejecuta el comando configurado para desbloquear la pantalla")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Desbloqueo exitoso"),
    })
    @RequestMapping(value = ApiPaths.UNLOCK_DESKTOP, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = SecurityConstants.AUTH_HEADER_NAME, dataType = "String",
            paramType = "header", required = false, value = "Access Token")
    public BaseResponseDTO unlock() {
        return desktopService.unlockDesktop();
    }
}
