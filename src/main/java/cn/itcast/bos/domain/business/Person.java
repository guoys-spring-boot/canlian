package cn.itcast.bos.domain.business;

/**
 * Created by gys on 2017/8/26.
 */
public class Person {

    private String id;

    private String name;

    private String sex;

    private String identity;

    private String deformity;

    private String bankid;

    private String address;

    private String phonenumber;

    private String leixing;

    private String canji;

    private String xingzhi;

    private String zhuangtai;

    private String beizhu;

    private String quyu;

    private String repeatFlag;

    private String uploadDate;

    public String getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(String repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getDeformity() {
        return deformity;
    }

    public void setDeformity(String deformity) {
        this.deformity = deformity;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public String getCanji() {
        return canji;
    }

    public void setCanji(String canji) {
        this.canji = canji;
    }

    public String getXingzhi() {
        return xingzhi;
    }

    public void setXingzhi(String xingzhi) {
        this.xingzhi = xingzhi;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getQuyu() {
        return quyu;
    }

    public void setQuyu(String quyu) {
        this.quyu = quyu;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", identity='" + identity + '\'' +
                ", deformity='" + deformity + '\'' +
                ", bankid='" + bankid + '\'' +
                ", address='" + address + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", leixing='" + leixing + '\'' +
                ", canji='" + canji + '\'' +
                ", xingzhi='" + xingzhi + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                ", beizhu='" + beizhu + '\'' +
                '}';
    }
}
