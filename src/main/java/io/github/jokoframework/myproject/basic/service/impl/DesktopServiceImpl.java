package io.github.jokoframework.myproject.basic.service.impl;

import io.github.jokoframework.common.errors.JokoApplicationException;
import io.github.jokoframework.myproject.basic.service.DesktopService;
import io.github.jokoframework.myproject.web.response.BaseResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class DesktopServiceImpl implements DesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopServiceImpl.class);

    @Value("${desktop.lockcommand}")
    private String lockCommand;

    @Value("${desktop.unlockcommand}")
    private String unlockCommand;

    @Override
    public BaseResponseDTO lockDesktop() {
        BaseResponseDTO response;
        if (StringUtils.isNotBlank(lockCommand)) {
            try {
                LOGGER.info("Blockeando el desktop con: {}", lockCommand);
                response = getResponseDTO(BaseResponseDTO.ok(), HttpStatus.OK);
                executeCommand(lockCommand);
            } catch (IOException | InterruptedException | JokoApplicationException e) {
                response = getResponseDTO(BaseResponseDTO.error(), HttpStatus.INTERNAL_SERVER_ERROR);
                response.setMessage(e.getMessage());
            }
        } else {
            LOGGER.error("No se encontró configuración para comando de bloqueo de escritorio: desktop.lockcommand");
            response = getResponseDTO(BaseResponseDTO.error(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public BaseResponseDTO unlockDesktop() {
        BaseResponseDTO response;
        if (StringUtils.isNotBlank(unlockCommand)) {
            try {
                LOGGER.info("Desblockeando el desktop con: {}", unlockCommand);
                response = getResponseDTO(BaseResponseDTO.ok(), HttpStatus.OK);
                executeCommand(unlockCommand);
            } catch (IOException | InterruptedException | JokoApplicationException e) {
                response = getResponseDTO(BaseResponseDTO.error(), HttpStatus.INTERNAL_SERVER_ERROR);
                response.setMessage(e.getMessage());
            }
        } else {
            LOGGER.error("No se encontró configuración para comando de desbloqueo de escritorio: desktop.unlockcommand");
            response = getResponseDTO(BaseResponseDTO.error(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private BaseResponseDTO getResponseDTO(BaseResponseDTO initialResponse, HttpStatus internalServerError) {
        BaseResponseDTO response;
        response = initialResponse;
        response.setHttpStatus(internalServerError);
        return response;
    }

    private void executeCommand(String commandLine) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("bash", "-c", commandLine);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        if (exitVal != 0) {
            throw new JokoApplicationException(
                    String.format("El comando %s no completó su ejecución exitosamente, código de error: %s",
                            commandLine, exitVal));
        }
    }
}
