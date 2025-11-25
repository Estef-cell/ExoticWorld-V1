$folders = Get-ChildItem C:\Users\ceore -Directory -ErrorAction SilentlyContinue
$results = @()

foreach($folder in $folders) {
    try {
        $size = (Get-ChildItem $folder.FullName -Recurse -File -ErrorAction SilentlyContinue | Measure-Object -Property Length -Sum).Sum
        $sizeGB = [math]::Round($size/1GB, 2)
        $results += [PSCustomObject]@{
            Folder = $folder.Name
            SizeGB = $sizeGB
        }
    } catch {
        # Skip folders with errors
    }
}

$results | Sort-Object SizeGB -Descending | Select-Object -First 20 | Format-Table -AutoSize
