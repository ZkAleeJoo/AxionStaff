$files = @(
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_en.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_es.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\config.yml"
)
foreach ($f in $files) {
    $content = Get-Content -Raw -Encoding UTF8 $f
    $matches = [regex]::Matches($content, '&#[Ff][Ff][0-9A-Fa-f]{4}|&c|&4')
    $unique = $matches | Select-Object -ExpandProperty Value -Unique
    Write-Host "File: $($f.Split('\')[-1]) - Reds: $($unique -join ', ')"
}
