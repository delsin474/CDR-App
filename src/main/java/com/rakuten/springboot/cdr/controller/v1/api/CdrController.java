package com.rakuten.springboot.cdr.controller.v1.api;

import com.rakuten.springboot.cdr.controller.v1.request.GetVolumeRequest;
import com.rakuten.springboot.cdr.controller.v1.request.ParseFileRequest;
import com.rakuten.springboot.cdr.dto.model.cdr.AnumChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.CallCategoryDurationDto;
import com.rakuten.springboot.cdr.dto.model.cdr.ServiceChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.SubscriberDto;
import com.rakuten.springboot.cdr.dto.response.Response;
import com.rakuten.springboot.cdr.service.CdrDetailService;

import com.rakuten.springboot.cdr.service.FileParserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Mohit Chandak
 */
@RestController
@RequestMapping("/api/v1/fileservice")
@Api(value = "cdr-application", description = "Operations pertaining CDR Analysis")
public class CdrController {

    @Autowired
    private FileParserService fileParserService;

    @Autowired
    private CdrDetailService cdrDetailService;

    @GetMapping("/listfiles")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getAllFiles() {
        return Response.ok().setPayload(fileParserService.getAllFiles());
    }

    @PostMapping("/parsefile")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getParseFile(@RequestBody @Valid ParseFileRequest parseFileRequest) {
        int crdRecords = fileParserService.parseFile(parseFileRequest.getFileName());
        if (crdRecords == -2) {
            return Response.notFound().setErrors(String.format("No Such File Found"));
        } else if (crdRecords == -1) {
            return Response.notFound().setErrors(String.format("File Data Already Present"));
        } else if (crdRecords > 0) {
            return Response.ok().setPayload(String.format("File Parsed Successfully and Data Stored to Database !!"));
        }
        return Response.notFound().setErrors(String.format("Requested File is Empty"));
    }

    @PostMapping("/getvolumebydateminutes")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getVolumeByDateMinutes(@RequestBody @Valid GetVolumeRequest getVolumeRequest) {
        Long totalVolume = cdrDetailService.getVolumePerDayMinutes(getVolumeRequest.getDate());
        return Response.ok().setPayload(totalVolume);
    }

    @PostMapping("/getvolumebydatemb")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getVolumeByDateMB(@RequestBody @Valid GetVolumeRequest getVolumeRequest) {
        Long totalVolume = cdrDetailService.getVolumePerDayMB(getVolumeRequest.getDate());
        return Response.ok().setPayload(totalVolume);
    }

    @GetMapping("/getanumhighestduration")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getAnumHighestDuration() {
        String anum = cdrDetailService.getAnumHighestDuration();
        return Response.ok().setPayload(anum + " has highest charge");
    }

    @GetMapping("/getvoicetotaldurationcallcat")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getVoiceTotalDurCallCat() {
        List<CallCategoryDurationDto> callDtos = cdrDetailService.getCallCategoryDuration();
        if (!callDtos.isEmpty()) {
            return Response.ok().setPayload(callDtos);
        }
        return Response.notFound().setErrors(String.format("No Such Records found"));
    }

    @GetMapping("/getanumhighestchargebyday")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getAnumHighestCharge() {
        List<AnumChargeDto> anumChargeDtos = cdrDetailService.getAnumByMaxChargePerDay();
        if (!anumChargeDtos.isEmpty()) {
            return Response.ok().setPayload(anumChargeDtos);
        }
        return Response.notFound().setErrors(String.format("No charges found"));
    }

    @GetMapping("/getvolsubstypegprs")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getVolSubscTypeGprs() {
        List<SubscriberDto> subDtos = cdrDetailService.getTotalVolumeBySubscriberTypeGprs();
        if (!subDtos.isEmpty()) {
            return Response.ok().setPayload(subDtos);
        }
        return Response.notFound().setErrors(String.format("No Records found"));
    }

    @GetMapping("/getservicehighestchargebyday")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getServiceHighestCharge() {
        List<ServiceChargeDto> serviceChargeDtos = cdrDetailService.getServiceByMaxChargePerDay();
        if (!serviceChargeDtos.isEmpty()) {
            return Response.ok().setPayload(serviceChargeDtos);
        }
        return Response.notFound().setErrors(String.format("No such charges found"));
    }

    @GetMapping("/generateoutputcdrfile")
    @ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
    public Response getOutputFile() {
        if (cdrDetailService.generateOutputJson())
            return Response.ok().setPayload(cdrDetailService.generateOutputJson());
        return Response.badRequest().setErrors(String.format("Error in Generating Output File"));    
    }

}
