package com.example.vnmh.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PriceChange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vnmh.ui.theme.ColorProvider

@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Giới thiệu",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 5.dp)
                        .align(Alignment.Start),
                ) {
                    // Hiển thị biểu tượng
                    Icon(
//                        imageVector = Icons.Outlined.Home,
                        imageVector = Icons.Outlined.Home,
                        tint = ColorProvider.mainColor,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.2f) // Điều chỉnh kích thước của biểu tượng
                            .padding(10.dp, 0.dp)
                    )
                    Text(
                        text = "Giờ mở cửa",
//                        modifier = Modifier.align(Alignment.Start),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
//                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Sáng: 8h đến 12h" +
                            "\nChiều: từ 13h30 đến 17h" +
                            "\nMở cửa tất cả các ngày trong tuần (Trừ Thứ Hai)",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 5.dp)
                        .align(Alignment.Start),
                ) {
                    // Hiển thị biểu tượng
                    Icon(
                        imageVector = Icons.Outlined.PriceChange,
                        tint = ColorProvider.mainColor,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.2f) // Điều chỉnh kích thước của biểu tượng
                            .padding(10.dp, 0.dp)
                    )
                    Text(
                        text = "Giá vé",
//                        modifier = Modifier.align(Alignment.Start),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Người lớn: 40.000đ/Người" +
                            "\nSinh viên: 20.000đ/Người" +
                            "\nHọc sinh: 10.000đ/Người" +
                            "\nTrẻ em dưới 6 tuổi và người khuyết tật đặc biệt nặng: Miễn phí",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 5.dp)
                        .align(Alignment.Start),
                ) {
                    // Hiển thị biểu tượng
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        tint = ColorProvider.mainColor,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.2f) // Điều chỉnh kích thước của biểu tượng
                            .padding(10.dp, 0.dp)
                    )
                    Text(
                        text = "Địa điểm",
//                        modifier = Modifier.align(Alignment.Start),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "216 Đ.Trần Quang Khải, Tràng Tiền, Hoàn Kiếm, Hà Nội",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 5.dp)
                        .align(Alignment.Start),
                ) {
                    // Hiển thị biểu tượng
                    Icon(
                        imageVector = Icons.Outlined.Feedback,
                        tint = ColorProvider.mainColor,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.2f) // Điều chỉnh kích thước của biểu tượng
                            .padding(10.dp, 0.dp)
                    )
                    Text(
                        text = "Phản hồi",
//                        modifier = Modifier.align(Alignment.Start),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Lắc điện thoại của bạn để mở biểu mẫu phản hồi. Hãy cho chúng tôi biết về trải nghiệm của bạn!",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorProvider.mainColor
                    )
                ) {
                    Text("Đóng")
                }
            }
        }
    }
}