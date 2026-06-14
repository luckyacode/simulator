$ErrorActionPreference = "Stop"

$ForwardTargets = @(
#     @{ Target = "svc/kafka-service";     LocalPort = 9092; RemotePort = 9092 }
#    @{ Target = "svc/kibana-service";    LocalPort = 5601; RemotePort = 5601 }
#     @{ Target = "svc/postgres-service";  LocalPort = 5432; RemotePort = 5432 }
   @{ Target = "deploy/simulator";      LocalPort = 8080; RemotePort = 8080 }
#    @{ Target = "deploy/pnr-gateway";    LocalPort = 8087; RemotePort = 8087 }
)

$BackgroundJobs = @()

Write-Host "=========================================================" -ForegroundColor Cyan
Write-Host "[INFO] Starting Global Port-Forwarding Pipeline..." -ForegroundColor Cyan
Write-Host "=========================================================" -ForegroundColor Cyan

foreach ($Item in $ForwardTargets) {
    $t = $Item.Target
    $lp = $Item.LocalPort
    $rp = $Item.RemotePort

    # Clean text concatenation to ensure no parser variable/drive mixups
    Write-Host "[LINK] Forwarding Local localhost:$lp ---> $t port $rp" -ForegroundColor Yellow

    $PortMapping = $lp.ToString() + ":" + $rp.ToString()
    $Args = @("port-forward", $t, $PortMapping, "--address=127.0.0.1")

    $Job = Start-Process kubectl -ArgumentList $Args -NoNewWindow -PassThru
    $BackgroundJobs += $Job
}

Write-Host "=========================================================" -ForegroundColor Cyan
Write-Host "[READY] All endpoints are connected and live!" -ForegroundColor Green
Write-Host "[STOP] PRESS [CTRL + C] IN THIS WINDOW TO DISCONNECT ALL" -ForegroundColor Magenta
Write-Host "=========================================================" -ForegroundColor Cyan

try {
    while ($true) {
        Start-Sleep -Seconds 1
    }
}
finally {
    Write-Host "`n[SHUTDOWN] Stopping active proxies safely..." -ForegroundColor Red
    foreach ($Job in $BackgroundJobs) {
        if ($Job -and -not $Job.HasExited) {
            Stop-Process -Id $Job.Id -Force -ErrorAction SilentlyContinue
        }
    }
    Write-Host "[DISCONNECTED] All pipelines killed clean." -ForegroundColor Red
}