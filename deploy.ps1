# --------------------------------------
# 🚀 Deploy Spring Boot App to Droplet
# --------------------------------------

# ----------- Config -------------
$sshKey = "C:\Users\Ali Computers\.ssh\id_rsa"
$jarName = "travel-partner-0.0.1-SNAPSHOT.jar"
$remoteUser = "root"
$remoteHost = "45.55.78.67"
$remotePath = "/opt/travel-partner"
$serviceName = "travel-partner.service"

# ----------- 1️⃣ Build JAR -------------
Write-Host "⚙️  Building JAR..."
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed! Exiting." -ForegroundColor Red
    exit 1
}

# ----------- 2️⃣ Backup Old JAR on Droplet -------------
Write-Host "💾 Backing up existing JAR on Droplet..."
ssh -i "$sshKey" "$remoteUser@$remoteHost" "cd $remotePath; if [ -f $jarName ]; then mv $jarName ${jarName}.bak; fi"

# ----------- 3️⃣ Upload New JAR -------------
Write-Host "📤 Uploading new JAR..."
$localJarPath = Join-Path -Path "target" -ChildPath $jarName
$remoteJarDestination = "$remoteUser@$remoteHost:`$remotePath/"
scp -i "$sshKey" "$localJarPath" "$remoteJarDestination"

# ----------- 4️⃣ Restart Spring Boot Service -------------
Write-Host "🔁 Restarting Spring Boot service..."
ssh -i "$sshKey" "$remoteUser@$remoteHost" "sudo systemctl restart $serviceName"

# ----------- 5️⃣ Verify Service -------------
Write-Host "✅ Checking service status..."
ssh -i "$sshKey" "$remoteUser@$remoteHost" "sudo systemctl status $serviceName -l --no-pager | head -20"

# ----------- 6️⃣ Follow Logs (Optional) -------------
Write-Host "📖 Following live logs... Press Ctrl+C to stop."
ssh -i "$sshKey" "$remoteUser@$remoteHost" "sudo journalctl -u $serviceName -f"