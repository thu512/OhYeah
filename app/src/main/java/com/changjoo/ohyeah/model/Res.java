package com.changjoo.ohyeah.model;

import java.util.ArrayList;

/**
 * Created by Changjoo on 2017-08-01.
 */

public class Res {
    int result;
    Doc doc;
    String msg;
    String error;

    @Override
    public String toString() {
        return "Res{" +
                "result=" + result +
                ", doc=" + doc +
                ", msg='" + msg + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Doc{
        String _id;
        int __v;
        Expenditure Expenditure;
        Asset Asset;
        Member Member;
        int n;
        int nModified;
        int ok;

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getnModified() {
            return nModified;
        }

        public void setnModified(int nModified) {
            this.nModified = nModified;
        }

        public int getOk() {
            return ok;
        }

        public void setOk(int ok) {
            this.ok = ok;
        }

        @Override
        public String toString() {
            return "Doc{" +
                    "_id='" + _id + '\'' +
                    ", __v=" + __v +
                    ", Expenditure=" + Expenditure +
                    ", Asset=" + Asset +
                    ", Member=" + Member +
                    ", n=" + n +
                    ", nModified=" + nModified +
                    ", ok=" + ok +
                    '}';
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public Expenditure getExpenditure() {
            return Expenditure;
        }

        public void setExpenditure(Expenditure expenditure) {
            this.Expenditure = expenditure;
        }

        public Asset getAsset() {
            return Asset;
        }

        public void setAsset(Asset asset) {
            this.Asset = asset;
        }

        public Member getMember() {
            return Member;
        }

        public void setMember(Member member) {
            this.Member = member;
        }

        public class Expenditure{
            ArrayList<Integer> Income;
            ArrayList<Integer> Expense;

            public ArrayList<Integer> getIncome() {
                return Income;
            }

            public void setIncome(ArrayList<Integer> income) {
                Income = income;
            }

            public ArrayList<Integer> getExpense() {
                return Expense;
            }

            public void setExpense(ArrayList<Integer> expense) {
                Expense = expense;
            }
        }
        public class Asset{
            int budget;
            int spare_money;
            int ratio_spare;
            int d_day;
            int first_daily_budget;
            int daily_budget;
            int set_date;
            String setb_yn;
            ArrayList<Fix_ex> Fix_ex;

            @Override
            public String toString() {
                return "Asset{" +
                        "budget=" + budget +
                        ", spare_money=" + spare_money +
                        ", ratio_spare=" + ratio_spare +
                        ", d_day=" + d_day +
                        ", first_daily_budget=" + first_daily_budget +
                        ", daily_budget=" + daily_budget +
                        ", set_date=" + set_date +
                        ", setb_yn='" + setb_yn + '\'' +
                        ", Fix_ex=" + Fix_ex +
                        '}';
            }

            public int getBudget() {
                return budget;
            }

            public void setBudget(int budget) {
                this.budget = budget;
            }

            public int getSpare_money() {
                return spare_money;
            }

            public void setSpare_money(int spare_money) {
                this.spare_money = spare_money;
            }

            public int getRatio_spare() {
                return ratio_spare;
            }

            public void setRatio_spare(int ratio_spare) {
                this.ratio_spare = ratio_spare;
            }

            public int getD_day() {
                return d_day;
            }

            public void setD_day(int d_day) {
                this.d_day = d_day;
            }

            public int getFirst_daily_budget() {
                return first_daily_budget;
            }

            public void setFirst_daily_budget(int first_daily_budget) {
                this.first_daily_budget = first_daily_budget;
            }

            public int getDaily_budget() {
                return daily_budget;
            }

            public void setDaily_budget(int daily_budget) {
                this.daily_budget = daily_budget;
            }

            public int getSet_date() {
                return set_date;
            }

            public void setSet_date(int set_date) {
                this.set_date = set_date;
            }

            public String getSetb_yn() {
                return setb_yn;
            }

            public void setSetb_yn(String setb_yn) {
                this.setb_yn = setb_yn;
            }

            public ArrayList<Doc.Asset.Fix_ex> getFix_ex() {
                return Fix_ex;
            }

            public void setFix_ex(ArrayList<Doc.Asset.Fix_ex> fix_ex) {
                Fix_ex = fix_ex;
            }

            public class Fix_ex{
                String f_ex_record;
                int f_ex_money;

                @Override
                public String toString() {
                    return "Fix_ex{" +
                            "f_ex_record='" + f_ex_record + '\'' +
                            ", f_ex_money=" + f_ex_money +
                            '}';
                }

                public String getF_ex_record() {
                    return f_ex_record;
                }

                public void setF_ex_record(String f_ex_record) {
                    this.f_ex_record = f_ex_record;
                }

                public int getF_ex_money() {
                    return f_ex_money;
                }

                public void setF_ex_money(int f_ex_money) {
                    this.f_ex_money = f_ex_money;
                }
            }
        }
        public class Member{
            String pw;
            String email;
            String del_yn;
            String setb_yn;

            public String getSetb_yn() {
                return setb_yn;
            }

            public void setSetb_yn(String setb_yn) {
                this.setb_yn = setb_yn;
            }

            @Override
            public String toString() {
                return "Member{" +
                        "pw='" + pw + '\'' +
                        ", email='" + email + '\'' +
                        ", del_yn='" + del_yn + '\'' +
                        ", setb_yn='" + setb_yn + '\'' +
                        '}';
            }

            public String getPw() {
                return pw;
            }

            public void setPw(String pw) {
                this.pw = pw;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDel_yn() {
                return del_yn;
            }

            public void setDel_yn(String del_yn) {
                this.del_yn = del_yn;
            }
        }

    }
}

