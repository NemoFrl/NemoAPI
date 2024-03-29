package nemofrl.nemoapi.entity;

import java.util.Date;

public class WpPosts {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.ID
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Long id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_author
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Long postAuthor;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_date
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Date postDate;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_date_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Date postDateGmt;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String postStatus;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.comment_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String commentStatus;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.ping_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String pingStatus;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_password
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String postPassword;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_name
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String postName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_modified
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Date postModified;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_modified_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Date postModifiedGmt;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_parent
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Long postParent;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.guid
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String guid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.menu_order
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Integer menuOrder;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String postType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.post_mime_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String postMimeType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.comment_count
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private Long commentCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column wp_posts.rss_id
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	private String rssId;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.ID
	 * @return  the value of wp_posts.ID
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.ID
	 * @param id  the value for wp_posts.ID
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_author
	 * @return  the value of wp_posts.post_author
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Long getPostAuthor() {
		return postAuthor;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_author
	 * @param postAuthor  the value for wp_posts.post_author
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostAuthor(Long postAuthor) {
		this.postAuthor = postAuthor;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_date
	 * @return  the value of wp_posts.post_date
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_date
	 * @param postDate  the value for wp_posts.post_date
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_date_gmt
	 * @return  the value of wp_posts.post_date_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Date getPostDateGmt() {
		return postDateGmt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_date_gmt
	 * @param postDateGmt  the value for wp_posts.post_date_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostDateGmt(Date postDateGmt) {
		this.postDateGmt = postDateGmt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_status
	 * @return  the value of wp_posts.post_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPostStatus() {
		return postStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_status
	 * @param postStatus  the value for wp_posts.post_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.comment_status
	 * @return  the value of wp_posts.comment_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getCommentStatus() {
		return commentStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.comment_status
	 * @param commentStatus  the value for wp_posts.comment_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.ping_status
	 * @return  the value of wp_posts.ping_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPingStatus() {
		return pingStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.ping_status
	 * @param pingStatus  the value for wp_posts.ping_status
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPingStatus(String pingStatus) {
		this.pingStatus = pingStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_password
	 * @return  the value of wp_posts.post_password
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPostPassword() {
		return postPassword;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_password
	 * @param postPassword  the value for wp_posts.post_password
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostPassword(String postPassword) {
		this.postPassword = postPassword;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_name
	 * @return  the value of wp_posts.post_name
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPostName() {
		return postName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_name
	 * @param postName  the value for wp_posts.post_name
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_modified
	 * @return  the value of wp_posts.post_modified
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Date getPostModified() {
		return postModified;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_modified
	 * @param postModified  the value for wp_posts.post_modified
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostModified(Date postModified) {
		this.postModified = postModified;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_modified_gmt
	 * @return  the value of wp_posts.post_modified_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Date getPostModifiedGmt() {
		return postModifiedGmt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_modified_gmt
	 * @param postModifiedGmt  the value for wp_posts.post_modified_gmt
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostModifiedGmt(Date postModifiedGmt) {
		this.postModifiedGmt = postModifiedGmt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_parent
	 * @return  the value of wp_posts.post_parent
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Long getPostParent() {
		return postParent;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_parent
	 * @param postParent  the value for wp_posts.post_parent
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostParent(Long postParent) {
		this.postParent = postParent;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.guid
	 * @return  the value of wp_posts.guid
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.guid
	 * @param guid  the value for wp_posts.guid
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.menu_order
	 * @return  the value of wp_posts.menu_order
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Integer getMenuOrder() {
		return menuOrder;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.menu_order
	 * @param menuOrder  the value for wp_posts.menu_order
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_type
	 * @return  the value of wp_posts.post_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPostType() {
		return postType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_type
	 * @param postType  the value for wp_posts.post_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostType(String postType) {
		this.postType = postType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.post_mime_type
	 * @return  the value of wp_posts.post_mime_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getPostMimeType() {
		return postMimeType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.post_mime_type
	 * @param postMimeType  the value for wp_posts.post_mime_type
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setPostMimeType(String postMimeType) {
		this.postMimeType = postMimeType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.comment_count
	 * @return  the value of wp_posts.comment_count
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public Long getCommentCount() {
		return commentCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.comment_count
	 * @param commentCount  the value for wp_posts.comment_count
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column wp_posts.rss_id
	 * @return  the value of wp_posts.rss_id
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public String getRssId() {
		return rssId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column wp_posts.rss_id
	 * @param rssId  the value for wp_posts.rss_id
	 * @mbg.generated  Mon Aug 12 12:32:11 CST 2019
	 */
	public void setRssId(String rssId) {
		this.rssId = rssId;
	}
}