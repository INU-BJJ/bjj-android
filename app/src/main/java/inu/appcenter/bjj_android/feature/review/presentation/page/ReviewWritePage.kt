package inu.appcenter.bjj_android.feature.review.presentation.page

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.component.dialog.ReviewCompletedDialog
import inu.appcenter.bjj_android.shared.component.error.ErrorHandler
import inu.appcenter.bjj_android.shared.component.noRippleClickable
import inu.appcenter.bjj_android.feature.review.presentation.ReviewViewModel
import inu.appcenter.bjj_android.feature.review.presentation.component.DashedBorderBox
import inu.appcenter.bjj_android.feature.review.presentation.component.DropdownMenuBox
import inu.appcenter.bjj_android.feature.review.presentation.component.DropdownType
import inu.appcenter.bjj_android.feature.review.presentation.component.ReviewTextField
import inu.appcenter.bjj_android.feature.review.presentation.component.StarRatingCalculatorBig
import inu.appcenter.bjj_android.feature.review.presentation.component.WriteComplete
import inu.appcenter.bjj_android.feature.review.presentation.component.addImageToList
import inu.appcenter.bjj_android.feature.review.presentation.component.getFileFromUri
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Red_FF3916
import inu.appcenter.bjj_android.shared.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewWriteScreen(
    onNavigateBack: () -> Unit,
    reviewViewModel: ReviewViewModel,
    navController: NavHostController? = null //
) {
    var cautionExpanded by remember { mutableStateOf(false) }
    var reviewComment by remember { mutableStateOf("") }
    var currentRating by remember { mutableIntStateOf(5) }
    val photos = remember { mutableStateListOf<Uri?>(null) }
    var currentCounting by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val imagePaths = photos.filterNotNull().map { uri ->
        getFileFromUri(context, uri)?.absolutePath
    }

    // 작성 완료 후 다이얼로그를 띄우기 위한 상태 변수
    var showCompletedDialog by remember { mutableStateOf(false) }

    // 갤러리에서 이미지 선택을 처리하는 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let {
                if (it.clipData != null) {
                    val clipData = it.clipData!!
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        addImageToList(uri, photos) {
                            currentCounting = it
                        }
                        if (currentCounting == 4) break
                    }
                } else {
                    val uri: Uri? = it.data
                    if (uri != null) {
                        addImageToList(uri, photos) { count ->
                            currentCounting = count
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            reviewViewModel.resetSelectedReviewRestaurant()
            reviewViewModel.resetSelectedMenu()
        }
    }

    // 새로운 ErrorHandler 컴포넌트 사용
    // NavController를 전달하거나, null을 전달하여 인증 만료 처리를 유연하게 대응
    ErrorHandler(
        viewModel = reviewViewModel,
        navController = navController // navController가 null이어도 작동
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.write_review_title),
                        style = LocalTypography.current.bold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = MaterialTheme.paddings.topBarPadding - MaterialTheme.paddings.iconOffset)
                            .offset(y = MaterialTheme.paddings.iconOffset)
                            .noRippleClickable { onNavigateBack() },
                        painter = painterResource(id = R.drawable.leftarrow),
                        contentDescription = stringResource(R.string.back_description)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.paddings.large, vertical = MaterialTheme.paddings.xsmall)
        ) {
            // 식당 위치 드롭다운
            DropdownMenuBox(
                reviewViewModel,
                dropdownType = DropdownType.RESTAURANT,
                labelText = stringResource(R.string.restaurant_location)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 메뉴 선택 드롭다운
            DropdownMenuBox(
                reviewViewModel,
                dropdownType = DropdownType.MENU,
                labelText = stringResource(R.string.menu_selection)
            )
            Spacer(modifier = Modifier.height(13.8.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.5.dp,
                color = Gray_B9B9B9
            )
            Spacer(modifier = Modifier.height(13.8.dp))

            Text(
                text = stringResource(R.string.rate_food_satisfaction),
                style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRatingCalculatorBig(
                    initialRating = 5f,
                    onRatingChanged = { newRating ->
                        currentRating = newRating
                    })
            }
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(R.string.write_detailed_review),
                style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            ReviewTextField(
                reviewComment,
                onTextChange = { newText ->
                    reviewComment = newText // 상태 업데이트
                }
            )
            Spacer(modifier = Modifier.height(15.dp))

            val arrangement =
                if (currentCounting == 4) Arrangement.SpaceBetween else Arrangement.Start

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = arrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                photos.forEachIndexed { index, uri ->
                    DashedBorderBox(
                        imageUri = uri,
                        onClickAddImage = {
                            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                                type = "image/*"
                                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 멀티 선택 허용
                            }
                            galleryLauncher.launch(intent)
                        },
                        showRemoveButton = (uri != null),
                        onRemoveImage = {
                            // 해당 이미지 박스를 제거
                            photos.removeAt(index)
                            currentCounting = photos.count { it != null }

                            // 4개 미만인데 null 없다면 null 하나 추가
                            if (currentCounting < 4 && photos.lastOrNull() != null) {
                                photos.add(null)
                            }

                            // 모두 제거되어 아무것도 없으면 null 하나 추가 (초기 상태 복원)
                            if (photos.isEmpty()) {
                                photos.add(null)
                            }
                        },
                        currentCounting = currentCounting
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
            Spacer(modifier = Modifier.height((44.7.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.review_caution_title),
                    style = LocalTypography.current.semibold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 18.sp,
                        color = Color.Black
                    )
                )
                Icon(
                    modifier = Modifier.clickable {
                        cautionExpanded = !cautionExpanded
                    },
                    painter = painterResource(R.drawable.underarrow),
                    contentDescription = stringResource(R.string.down_arrow),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(23.dp))

            if (cautionExpanded) {
                AnimatedVisibility(
                    visible = cautionExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.restaurant_not_open_warning),
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Red_FF3916
                    )
                }
            } else {
                AnimatedVisibility(
                    visible = cautionExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "",
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Red_FF3916
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            WriteComplete(
                reviewComment = reviewComment,
                currentRating = currentRating,
                reviewViewModel = reviewViewModel,
                selectedImages = imagePaths,
                onSuccess = {
                    // 리뷰 작성 성공 시 다이얼로그 상태 true로 변경
                    showCompletedDialog = true
                }
                // 에러 콜백은 생략 가능 - BaseViewModel과 ErrorHandler가 자동으로 처리
            )
        }

        // 작성 완료 다이얼로그
        ReviewCompletedDialog(
            show = showCompletedDialog,
            onDismiss = {
                showCompletedDialog = false
                onNavigateBack()
            }
        )
    }
}