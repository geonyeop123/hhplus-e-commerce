package kr.hhplus.be.server.interfaces.point;


import jakarta.validation.Valid;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PointController implements PointDocs{

    private final PointService pointService;

    @GetMapping("/api/v1/users/{userId}/points")
    public ResponseEntity<PointResponse> point(
            @PathVariable Long userId
            ) {
        return ResponseEntity.ok(PointResponse.from(pointService.find(userId)));
    }

    @PostMapping("/api/v1/users/{userId}/points")
    public ResponseEntity<PointResponse> charge(@PathVariable Long userId,
                                                @Valid @RequestBody PointRequest.Charge request) {
        PointCommand.Charge command = request.toCommand(userId);
        return ResponseEntity.ok(PointResponse.from(pointService.charge(command)));
    }

}
