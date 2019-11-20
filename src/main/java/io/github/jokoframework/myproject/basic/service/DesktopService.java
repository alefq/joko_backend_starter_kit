package io.github.jokoframework.myproject.basic.service;

import io.github.jokoframework.myproject.web.response.BaseResponseDTO;

public interface DesktopService {

    BaseResponseDTO lockDesktop();

    BaseResponseDTO unlockDesktop();
}
