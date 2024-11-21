package inu.appcenter.bjj_android.ui.review.reviewPagePart

data class ReviewResponse(
    val reviewDetailList: List<ReviewDetail>,
    val lastPage: Boolean
)

data class ReviewDetail(
    val reviewId: Int,
    val comment: String,
    val rating: Int,
    val imagePaths: String,
    val likeCount: Int,
    val createdDate: String,
    val menuPairId: Int,
    val mainMenuId: Int,
    val subMenuId: Int,
    val memberId: Int,
    val memberNickname: String,
    val memberImagePath: String,
    val likedMenu: Boolean,
    val restaurantName: String,
    val restaurantId: Int,
    val menuPairName: String
)

val tempResponse = ReviewResponse(
    reviewDetailList = listOf(
        ReviewDetail(
            reviewId = 1,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 1,
            createdDate = "2024-08-20",
            menuPairId = 1,
            mainMenuId = 1,
            subMenuId = 1,
            memberId = 1,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "학생식당",
            restaurantId = 1,
            menuPairName = "우삼겹떡볶이*핫도그"
        ),
        ReviewDetail(
            reviewId = 6,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 2,
            createdDate = "2024-08-20",
            menuPairId = 6,
            mainMenuId = 6,
            subMenuId = 6,
            memberId = 6,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "학생식당",
            restaurantId = 1,
            menuPairName = "불고기파스타/단무지"
        ),
        ReviewDetail(
            reviewId = 2,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 2,
            createdDate = "2024-08-20",
            menuPairId = 2,
            mainMenuId = 2,
            subMenuId = 2,
            memberId = 2,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "학생식당",
            restaurantId = 1,
            menuPairName = "우삼겹떡볶이*핫도그"
        ),
        ReviewDetail(
            reviewId = 3,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 3,
            createdDate = "2024-08-20",
            menuPairId = 3,
            mainMenuId = 3,
            subMenuId = 3,
            memberId = 3,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "학생식당",
            restaurantId = 1,
            menuPairName = "우삼겹떡볶이*핫도그"
        ),
        ReviewDetail(
            reviewId = 4,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 4,
            createdDate = "2024-08-20",
            menuPairId = 4,
            mainMenuId = 4,
            subMenuId = 4,
            memberId = 4,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "2호관 교직원식당",
            restaurantId = 2,
            menuPairName = "불고기파스타/우동국물"
        ),
        ReviewDetail(
            reviewId = 6,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 5,
            createdDate = "2024-08-20",
            menuPairId = 5,
            mainMenuId = 5,
            subMenuId = 5,
            memberId = 5,
            memberNickname = "Tracey jun",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "2호관 교직원식당",
            restaurantId = 2,
            menuPairName = "김치찌개"
        ),
        ReviewDetail(
            reviewId = 5,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 5,
            createdDate = "2024-08-20",
            menuPairId = 5,
            mainMenuId = 5,
            subMenuId = 5,
            memberId = 5,
            memberNickname = "Tracey jun",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "2호관 교직원식당",
            restaurantId = 2,
            menuPairName = "우삼겹떡볶이*핫도그"
        ),
        ReviewDetail(
            reviewId = 6,
            comment = "invenire",
            rating = 4,
            imagePaths = "nunc",
            likeCount = 2,
            createdDate = "2024-08-20",
            menuPairId = 6,
            mainMenuId = 6,
            subMenuId = 6,
            memberId = 6,
            memberNickname = "Tracey Romero",
            memberImagePath = "efficiantur",
            likedMenu = false,
            restaurantName = "학생식당",
            restaurantId = 1,
            menuPairName = "불고기파스타/단무지"
        )
    ),
    lastPage = false
)




val tempReviews = tempResponse

