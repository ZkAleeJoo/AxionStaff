$files = @(
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_en.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\lang\messages_es.yml",
    "c:\Users\xxale\Documents\GitHub\AxionStaff\src\main\resources\config.yml"
)
foreach ($f in $files) {
    $content = Get-Content -Raw -Encoding UTF8 $f
    $newContent = [regex]::Replace($content, '&#[Ff][Ff]3333|&#[Ff][Ff]5555|&c|&4', '&#2F09DB')
    # Write back without BOM by using UTF8NoBOM or just default out-file
    [IO.File]::WriteAllText($f, $newContent, [Text.Encoding]::UTF8)
    Write-Host "Replaced in $($f.Split('\')[-1])"
}
