package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.CreateOccupancyRequest;
import si.feri.itk.projectmanager.dto.request.UpdateOccupancyRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.dto.response.occupancy.CreateOccupancyResponse;
import si.feri.itk.projectmanager.service.OccupancyService;

import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("/occupancy")
@RequiredArgsConstructor
public class OccupancyController {
    private final OccupancyService occupancyService;

    @PostMapping
    public ResourceCreatedResponse addOccupancy(@RequestBody CreateOccupancyRequest request, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        CreateOccupancyResponse response = occupancyService.addOccupancy(request, servletRequest);
        if (response != null && response.getIds() != null && !response.getIds().isEmpty()) {
            servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
        return response;
    }


    @PatchMapping("/{occupancyId}")
    public void updateOccupancy(@RequestBody UpdateOccupancyRequest request, HttpServletRequest servletRequest) {
        occupancyService.updateOccupancy(request, servletRequest);
    }

    @DeleteMapping("/{occupancyId}")
    public void deleteOccupancy(@PathVariable @NotNull UUID occupancyId, HttpServletRequest servletRequest) {
        occupancyService.deleteOccupancy(occupancyId, servletRequest);
    }


}
