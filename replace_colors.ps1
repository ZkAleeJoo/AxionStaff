$files = @(
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_en.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_es.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\config.yml"
)
foreach ($f in $files) {
    $content = Get-Content -Raw -Encoding UTF8 $f
    $newContent = [regex]::Replace($content, '&#2[Ff]09[Dd][Bb]', '&#441BFA')
    [IO.File]::WriteAllText($f, $newContent, [Text.Encoding]::UTF8)
    Write-Host "Replaced in $($f.Split('\')[-1])"
}
