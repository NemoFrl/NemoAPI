package nemofrl.nemoapi.entity;

public class WpTermRelationships extends WpTermRelationshipsKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wp_term_relationships.term_order
     *
     * @mbg.generated Sun Aug 11 01:12:21 CST 2019
     */
    private Integer termOrder;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wp_term_relationships.term_order
     *
     * @return the value of wp_term_relationships.term_order
     *
     * @mbg.generated Sun Aug 11 01:12:21 CST 2019
     */
    public Integer getTermOrder() {
        return termOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wp_term_relationships.term_order
     *
     * @param termOrder the value for wp_term_relationships.term_order
     *
     * @mbg.generated Sun Aug 11 01:12:21 CST 2019
     */
    public void setTermOrder(Integer termOrder) {
        this.termOrder = termOrder;
    }
}