package cn.eoe.app.entity;

/**
 * 动态显示详情页面的操作栏实体类
 *
 */
public class DetailBarEntity {
	
	private UserLikeEntity userlike;//赞
	private CommentEntity	comment;//评论
	private String	favorite;//收藏
	
	/**
	 * 赞
	 */
	public UserLikeEntity getUserlike() {
		return userlike;
	}
	/**
	 * 赞
	 */
	public void setUserlike(UserLikeEntity userlike) {
		this.userlike = userlike;
	}
	/**
	 * 评论
	 */
	public CommentEntity getComment() {
		return comment;
	}
	/**
	 * 评论
	 */
	public void setComment(CommentEntity comment) {
		this.comment = comment;
	}
	/**
	 * 收藏
	 */
	public String getFavorite() {
		return favorite;
	}
	/**
	 * 收藏
	 */
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
}
