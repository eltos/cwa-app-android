package de.rki.coronawarnapp.covidcertificate.recovery.core.qrcode

import de.rki.coronawarnapp.covidcertificate.common.certificate.DccData
import de.rki.coronawarnapp.covidcertificate.common.certificate.RecoveryDccV1
import de.rki.coronawarnapp.covidcertificate.common.qrcode.DccQrCode
import de.rki.coronawarnapp.covidcertificate.common.qrcode.QrCodeString
import de.rki.coronawarnapp.util.HashExtensions.toSHA256

data class RecoveryCertificateQRCode(
    override val qrCode: QrCodeString,
    override val data: DccData<RecoveryDccV1>,
) : DccQrCode {
    override val uniqueCertificateIdentifier: String
        get() = qrCode.toSHA256()
}
