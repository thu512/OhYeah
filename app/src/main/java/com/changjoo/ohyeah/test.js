//$ npm install trim
//$ component install component/trim

//일일 사용가능 금액 표시 [{총예산-(고정지출+비상금)}/ 정산일 - 오늘]
var express = require('express');
var async = require('async');
var _ = require('lodash');
var router = express.Router();
var UserModel = require('../models/users');
var apiKey = 'AAAArxogoKw:APA91bEf_z6T5jly-SDqELNjWMHytfZutTZOTFp92SW62mMs3wE8NvhZ2_hyuP7dAIfI29w8EfITPDBEDCAFCI903c0n7MO6sSw3xD40vqcYI0IaoLh_yqrex9CkrufClneYxwFHkHYw';
var FCM    = require('fcm').FCM;
var fcm    = new FCM(apiKey);

//하루 지출 가능금액 & 지출내역 조회
router.post('/today_view', function(req, res, next){
   var email = req.body.email;
   // var today = new Date();
   // var month = today.getMonth() + 1;
   // if(month < 10)
   //    month = String(0) + String(month);
   // var todayDate= month+"/"+today.getDate();

   UserModel.findOne({'Member.email':email}, function(err, doc){
      //var data={};
      // if(err) console.log('get update err=', err);
      // console.log('get update doc=',doc,'doc.Expenditure.Expense.length=',doc.Expenditure.Expense.length);
      // console.log('doc.Expenditure.Expense[0].date=',doc.Expenditure.Expense[0].date);
      // var j=0;
      // for(i=0;i<doc.Expenditure.Expense.length;i++){
      //    console.log("today=",todayDate,"doc.Expenditure.Expense[",i,"].date=",doc.Expenditure.Expense[i].date);
      //    if(doc.Expenditure.Expense[i].date==todayDate){
      //       data[j]=doc.Expenditure.Expense[i];
      //       j++;
      //       console.log("data[",j,"]=",data[j],"doc.Expenditure.Expense[i]",doc.Expenditure.Expense[i]);
      //    }
      // }
      res.json({doc:doc});
   });
});

//한달 지출 가능금액 & 지출내역 조회
router.post('/month_view', function(req, res, next){
   var email = req.body.email;
   // var today = new Date();
   // var month = today.getMonth() + 1;
   // var data={};
   // if(month < 10)
   //    month = String(0) + String(month);

   UserModel.findOne({'Member.email':email}, function(err, doc){
      // if(err) console.log('get update err=', err);
      // console.log('get update doc=',doc);
      // console.log('doc.Expenditure.Expense.length',doc.Expenditure.Expense.length);
      // var j=0;
      // for(i=0;i<doc.Expenditure.Expense.length;i++){
      //    var date=doc.Expenditure.Expense[i].date.slice(0,2);

      //    console.log('date',date,"doc.Expenditure.Expense[",i,"].date=",doc.Expenditure.Expense[i].date);
      //    if(date==month){
      //       data[j]=doc.Expenditure.Expense[i];
      //       j++;
      //       console.log("data[",j,"]=",data[j],"doc.Expenditure.Expense[i]",doc.Expenditure.Expense[i]);
      //    }
      // }
      res.json({doc:doc});
   });
});


//하루 지출
router.post('/today_expend', function(req, res, next){
   console.log('req.body=', req.body);
   // var ex_date = req.body.ex_date; // 2017.08.07
   //var ex_date = req.body.ex_date; // "07/31 11:57"
   //var ex_record = req.body.ex_record; // "출금"
   //var ex_money = req.body.ex_money; // 10000
   var email = req.body.email;
   var msg=req.body.msg;
  // var flag=0;
   var woori="우리";
   var kb="KB";
   var expend = msg.split(' '&&'\n');
   console.log('expend=',expend);
   var data1 = expend[1];
   var data2 = data1.split(' ');
   var data3 = expend[3].split(' ');
   var data4 = expend[4].split(' ');
   console.log('date1',data1);
   console.log('date2',data2);
   console.log('date3',data3);
   console.log('date4',data4);
   console.log(_.includes(expend[1],woori));
   console.log(_.includes(expend[1],kb));

   if(_.includes(expend[1],woori)==true){
   var data1=expend[1];
   var data2 = data1.split(' ');
   var data3 = expend[3].split(' ');
   console.log('date2',data2);
   console.log('date3',data3);
   var date=data2[1];
   var time=data2[2].replace(" ",'');
   var ex_in=data3[0].replace(" ",'');
   var money=data3[1].replace("원",'');
   money=money.replace(",",'');
   var record=expend[4].replace(" ",'');
   console.log('date=',date,'time=',time,'ex_in=',ex_in,'money=',money,'record=',record);
   }
   else //if(_.includes(expend[1],kb))
   {
   var date=data3[0];
   console.log("date",date);
   var time=data3[1]//.replace(" ",'');
   var ex_in=data3[0].replace(" ",'');
   var money=expend[4].replace("원",'');
   money=money.replace(",",'');
   var record=expend[5].replace(" ",'');
   console.log('date=',date,'time=',time,'ex_in=',ex_in,'money=',money,'record=',record);
   }


   // UserModel.findOne({'Member.email':email}, function(err,doc){
   //    var count_f_db = doc.Asset.Fix_ex.length;
   //    var down=doc.Asset.Fix_ex[i]*1.2;
   //    var up=doc.Asset.Fix_ex[i]*0.8;

   //    console.log("length=",count_f_db);
   //    for (var i = 0 ; i < count_f_db ; i++) {
   //       if( money>=down && money>=up){
   //        console.log("고정지출 확인!");
   //        res.json({doc:doc, msg:"고정지출인지 확인"})
   //       }
   //    }
   // });

   if(record.indexOf("#")!=-1)
      record="ATM출금"
   console.log('date=',date,'time=',time,'ex_in=',ex_in,'money=',money,'record=',record);

   if(ex_in=='출금'){
   UserModel.findOne({'Member.email' : email}, function(err, doc){
      for(var i=0; i<doc.Asset.Fix_ex.length; i++){
         console.log("doc.Asset.Fix_ex[",i,"].f_ex_money=",doc.Asset.Fix_ex[i].f_ex_money);
         if( money <= doc.Asset.Fix_ex[i].f_ex_money * 1.2 && money >= doc.Asset.Fix_ex[i].f_ex_money * 0.8){
          //  flag=1;
            console.log("고정지출입니다.");
            console.log("고정지출 푸시 메시지",doc.Member.toekn);
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 8,
               title:"고정지출 확인",
               body: time+" 출금된 "+ record+" "+money+"원은 고정지출 "+doc.Asset.Fix_ex[i].f_ex_record+"내역이 맞나요?",
               fix: {
                  msg_content: record,
                  msg_money: money,
                  msg_date: date,
                  msg_time: time,
                  fix_data: doc.Asset.Fix_ex[i].f_ex_record
               }
               })
            };
            console.log("data=",message);
            fcm.send(message, function(err, messageId){
               if (err) {
               console.log("고정지출 푸시 에러! Something has gone wrong!");
               } else {
               console.log("고정지출인지 확인하세요. message ID: ", messageId);
               }
            });//send
            console.log("고정지출 푸시 메시지 전송 완료");
         }//if
         }//for
        // console.log('flag=',flag);
      //console.log('doc=', doc);
      //console.log('doc.Expenditure.Expense=', doc.Expenditure.Expense);
      //var budget = doc.Asset.budget;
      //var d_day = doc.Asset.d_day;
//      if(flag=0){
      var total_ex_money = doc.Asset.total_ex_money;
      //console.log('budget=', budget, 'd_day', d_day);
      var data = {
         date: date,
         time: time,
         record: record,
         money: money,
         ex_in: "출금"
      };
      doc.Expenditure.Expense.push(data);
      //console.log('before total expend =', doc.Asset.total_ex_money);
      //res.json({doc:doc});
      doc.save(function(err){
         // doc.Asset.total_ex_money = total_ex_money + ex_money;
         // doc.Asset.daily_budget = Math.floor((budget-total_ex_money)/d_day);
         // console.log('after total expend =', doc.Asset.total_ex_money);
         var arr = doc.Expenditure.Expense;
         //var date = doc.Expenditure.Expense.ex_date.slice(0,-2);
         var sum = 0;
         for(var i=0; i < arr.length; i++) {
          //  if(date== Date().getDate()){
            console.log('arr[i].money=', arr[i].money);
            sum += arr[i].money;
           // }
         }//for
         console.log('sum=', sum);
         doc.Expenditure.total_ex_money = sum;
         doc.Asset.daily_budget = Math.floor(doc.Asset.daily_budget-money);
         doc.Asset.budget = Math.floor(doc.Asset.budget-money);
         console.log("daily_budget=",doc.Asset.daily_budget);
         if(doc.Asset.daily_budget<=0){
            console.log("푸시 메시지",doc.Member.toekn);
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 2,
               title:"오늘 예산 소진",
               body:"오늘 하루 남은 예산이 없어요."
               })
            };
            console.log("푸시 메시지 생성 완료");
            fcm.send(message, function(err, messageId){
               if (err) {
               console.log("초과!!!!! Something has gone wrong!");
               } else {
               console.log("초과!!!!! 비상금을 추가하세요. message ID: ", messageId);
               }
            });
            console.log("푸시 메시지 전송 완료");
         }
         else if(doc.Asset.daily_budget <= doc.Asset.first_daily_budget * 0.2){
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 1,
               title:"잔여 예산 "+doc.Asset.daily_budget+"원",
               body:"하루동안 지출을 줄여보세요"
               })
            };
             fcm.send(message, function(err, messageId){
               if (err) {
               console.log("경고! Something has gone wrong!");
               } else {
               console.log("경고! 오늘 하루 예산이 20% 남았습니다. message ID: ", messageId);
               }
            });
          }
         else
            console.log("정상");
         doc.save(function(err){
            res.json({doc:doc});
         });

      });
 //  }
   });
}
   else {
      UserModel.findOne({'Member.email' : email}, function(err, doc){
      //console.log('doc=', doc);
      //console.log('doc.Expenditure.Expense=', doc.Expenditure.Expense);
     // var budget = doc.Asset.budget;
      //var d_day = doc.Asset.d_day;
     // var total_ex_money = doc.Asset.total_ex_money;
      //console.log('budget=', budget, 'd_day', d_day);
      var data = {
         date: date,
         time: time,
         record: record,
         money: money,
         ex_in: "입금"
      };
      doc.Expenditure.Expense.push(data);
      console.log('data=',data);
      //console.log('before total expend =', doc.Asset.total_ex_money);

      //res.json({doc:doc});
      doc.save(function(err){
         // doc.Asset.total_ex_money = total_ex_money + ex_money;
         // doc.Asset.daily_budget = Math.floor((budget-total_ex_money)/d_day);
         // console.log('after total expend =', doc.Asset.total_ex_money);
         var arr = doc.Expenditure.Expense;
         //var date = doc.Expenditure.Expense.ex_date.slice(0,-2);
         var sum = 0;
         for(var i=0; i < arr.length; i++) {
          //  if(date== Date().getDate()){
            console.log('arr[i].money=', arr[i].money);
            sum += arr[i].money;
           // }
         }//for
         console.log('sum=', sum);
         //doc.Expenditure.total_ex_money = sum;
         doc.Asset.daily_budget = Math.floor(doc.Asset.daily_budget+Number(money));
         doc.Asset.budget = Math.floor(doc.Asset.budget+Number(money));
         console.log('daily_budget=', doc.Asset.daily_budget);
         if(doc.Asset.daily_budget<=0){
            console.log("푸시 메시지");
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 2,
               title:"오늘 예산 소진",
               body:"오늘 하루 남은 예산이 없어요."
               })
            };
            console.log("푸시 메시지 생성 완료");
            fcm.send(message, function(err, messageId){
               if (err) {
               console.log("초과!!!!! Something has gone wrong!");
               } else {
               console.log("초과!!!!! 비상금을 추가하세요 ID: ", messageId);
               }
            });
            console.log("푸시 메시지 전송 완료");
       }
       else if(doc.Asset.daily_budget <= doc.Asset.first_daily_budget * 0.2){
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 1,
               title:"잔여 예산 "+doc.Asset.daily_budget+"원",
               body:"하루동안 지출을 줄여보세요"
               })
            };
             fcm.send(message, function(err, messageId){
               if (err) {
               console.log("경고! Something has gone wrong!");
               } else {
               console.log("경고! 오늘 하루 예산이 20% 남았습니다.Sent with message ID: ", messageId);
               }
            });
          }
         else
            console.log("정상");
         doc.save(function(err){
            res.json({doc:doc});
         });

      });
   });
   }

});

//한달 main
/*router.post('/month_expend', function(req, res, next){
   console.log('req.body=', req.body);
   // var ex_date = req.body.ex_date; // 2017.08.07
   var ex_date = req.body.ex_date; // "07/31 11:57"
   var ex_record = req.body.ex_record; // "출금"
   var ex_money = req.body.ex_money; // 10000
   var email = req.body.email;

   UserModel.findOne({'Member.email' : email}, function(err, doc){
      console.log('doc=', doc);
      console.log('doc.Expenditure.Expense=', doc.Expenditure.Expense);
      var budget = doc.Asset.budget;
      var d_day = doc.Asset.d_day;
      var total_ex_money = doc.Asset.total_ex_money;
      console.log('budget=', budget, 'd_day', d_day);
      var data = {
         ex_date: ex_date,
         ex_record: ex_record,
         ex_money: ex_money
      };
      doc.Expenditure.Expense.push(data);
      console.log('before total expend =', doc.Asset.total_ex_money);

      //res.json({doc:doc});
      doc.save(function(err){
         // doc.Asset.total_ex_money = total_ex_money + ex_money;
         // doc.Asset.daily_budget = Math.floor((budget-total_ex_money)/d_day);
         // console.log('after total expend =', doc.Asset.total_ex_money);
         var arr = doc.Expenditure.Expense;
         var sum = 0;
         for(var i=0; i < arr.length; i++) {
            console.log('arr[i].ex_money=', arr[i].ex_money);
            sum += arr[i].ex_money;
         }//for
         console.log('sum=', sum);
         doc.Expenditure.month_total_ex = sum;
         doc.save(function(err){
            res.json({doc:doc});
         });
      });
   });
});*/

//매일 12시마다 일일 사용가능 지출금액 초기화
var cron = require('cron');
new cron.CronJob('45 10 10 * * *', function(err){
UserModel.find({}, function(err, docs){
   var today = new Date();
   var year = today.getFullYear();
   if(err) console.log('err=', err);
   // console.log('docs=', docs);
   console.log('docs[0].Asset.set_date=', docs[0].Asset.set_date);
   async.eachSeries(docs, function(doc, callback) {
        //d-day 계산
        var set_day= doc.Asset.set_date;  //db에서 월급일(set_date)를 가져옴
        console.log("set_day=",set_day);

        //월급일자이 오늘 일자보다 나중이면 이번달, 이전이면 다음달
        if(set_day>=today.getDate())
         var set_month= today.getMonth()+1;
        else
            var set_month= today.getMonth()+2;

        //이번달이 12월이면 내년 세팅
        if(set_month==1)
            var set_year=today.getFullYear()+1;
        else
            var set_year=today.getFullYear();


        var d_str= new Date(String(year+'-'+set_month+'-'+(set_day+1)));//월급날짜
        var diff=d_str.getTime()-today.getTime();  //월급날짜-오늘
        var days= Math.floor(diff/(1000*60*60*24));   //d-day 계산
        doc.Asset.d_day = days;
        console.log("월급일=",d_str,"오늘=",today,"d-day=",days);

        //오늘 사용가능 금액 세팅
        if(doc.Asset.daily_budget < 0) { //어제 남은 일일사용금액이 - 일 경우, 초과사용한 경우
            doc.Asset.budget=doc.Asset.budget-doc.Asset.first_daily_budget+doc.Asset.daily_budget; //남은 한달 예산=남은 한달 사용금액-일일사용가능금액-초과한 금액
            doc.Asset.first_daily_budget=doc.Asset.daily_budget=Math.floor(doc.Asset.budget/doc.Asset.d_day/10)*10;  //오늘 사용가능한금액
            }
        else if(doc.Asset.daily_budget > 0){ //어제 남은 일일사용금액이 +일 경우, 남은 경우
            doc.Goal.recent_saving=(doc.Goal.recent_saving+doc.Asset.daily_budget)/2;//최근 저금액을 오늘 남은 금액으로 평균 계산해서 저장
            doc.Goal.now_saving = doc.Goal.now_saving+doc.Asset.daily_budget; //기존 저금액에 남은 금액 더해줌
            doc.Goal.ratio_saving=Math.floor(doc.Goal.now_saving/doc.Goal.goal_money*100); //목표달성률 다시 계산
            console.log("now_saving=",doc.Goal.now_saving,"goal_money=",doc.Goal.goal_money,"ratio_saving=",doc.Goal.ratio_saving)
            doc.Asset.budget=doc.Asset.budget-doc.Asset.first_daily_budget;//남은 한달 예산=남은 한달 사용금액-일일사용가능금액
            doc.Asset.first_daily_budget=doc.Asset.daily_budget=Math.floor(doc.Asset.budget/doc.Asset.d_day/10)*10;  //오늘 사용가능한금액
            }
        else {
            doc.Asset.budget=doc.Asset.budget-doc.Asset.first_daily_budget;//남은 한달 예산=남은 한달 사용금액-일일사용가능금액
            doc.Asset.first_daily_budget=doc.Asset.daily_budget=Math.floor(doc.Asset.budget/doc.Asset.d_day/10)*10;  //오늘 사용가능한금액
        }

        //오늘,이번달 여부 표시
      //console.log('doc=', doc);
      // console.log('before doc.Expenditure.Expense=', doc.Expenditure.Expense);
      for(var i = 0; i < doc.Expenditure.Expense.length; i++) {
         doc.Expenditure.Expense[i].today_yn = 'N';
         if(today.getDate()==doc.Asset.set_date){
            doc.Expenditure.Expense[i].month_yn = 'N';
         }
      }//for
      // console.log('after doc.Expenditure.Expense=', doc.Expenditure.Expense);
      console.log("doc.Goal.now_saving=",doc.Goal.now_saving,"doc.Goal.goal_money=",doc.Goal.goal_money);
      if(doc.Goal.now_saving >= doc.Goal.goal_money){
            console.log("목표금액 달성");
            var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 3,
               title:"목표 달성을 축하합니다!",
               body:"저금하신 금액은 "+doc.Goal.now_saving+"원 입니다:)"
               })
            };
            console.log("message=",message);
            fcm.send(message, function(err, messageId){
               if (err) {
               console.log("목표 달성 푸쉬 에러! Something has gone wrong!");
               } else {
               console.log("목표 달성을 축하합니다! Sent with message ID: ", messageId);
               }
            });
          }
      doc.save(function(err){
         if(err) console.log('err=', err);
         callback();
      });


   }, function(err) {
      console.log('목표달성');

   });
});
   }, false, "Asia/Seoul");

//9시마다 하루 예산 확인, 한달에 한번 9시마다 예산 설정
new cron.CronJob('00 00 09 * * *', function(err){
UserModel.find({}, function(err, docs){
   var today = new Date();
   var month= today.getMonth();
   if(err) console.log('err=', err);
   // console.log('docs=', docs);
   console.log('docs[0].Asset.set_date=', docs[0].Asset.set_date);
   async.eachSeries(docs, function(doc, callback) {
      if(today.getDate()==doc.Asset.set_date){
         var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 4,
               title: month+"달 예산 설정",
               body:"이번달 예산을 설정해주세요."
               })
            };
         console.log("message=",message);
         fcm.send(message, function(err, messageId){
            if (err) {
               console.log("이번달 예산 설정 푸쉬 에러! Something has gone wrong!");
            } else {
               console.log("이번달 예산 설정 ! Sent with message ID: ", messageId);
               }
            });
      }
      var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 5,
               title:"오늘 예산 알림",
               body:"오늘 예산은 "+doc.Asset.first_daily_budget+"원이에요"
               })
            };
         console.log("message=",message);
         fcm.send(message, function(err, messageId){
            if (err) {
               console.log("이번달 예산 설정 푸쉬 에러! Something has gone wrong!");
            } else {
               console.log("이번달 예산 설정 ! Sent with message ID: ", messageId);
               }
            });
      callback();

   }, function(err) {
      console.log('목표달성');
   });
});
   }, false, "Asia/Seoul");

//저금 푸시
new cron.CronJob('15 58 16 * * *', function(err){
UserModel.find({}, function(err, docs){
   if(err) console.log('err=', err);
   // console.log('docs=', docs);
   console.log('docs[0].Asset.set_date=', docs[0].Asset.set_date);
   async.eachSeries(docs, function(doc, callback) {
      console.log('doc.Asset.daily_budget=', doc.Asset.daily_budget);
      if(doc.Asset.daily_budget>0){
          var message = {
            registration_id: doc.Member.token, // required
            collapse_key: ''+ Math.floor(Math.random() * 1000),
            data: JSON.stringify({
               result: 7,
               title:"저금 안내",
               body:"오늘 예산에서 남은 금액 "+doc.Asset.daily_budget+"원은 자정에 저금될거에요."
               })
            };
         console.log("message=",message);
         fcm.send(message, function(err, messageId){
               if (err) {
               console.log("에러! 저금푸시 실패!",err);
               callback();
               } else {
               console.log("저금푸시 성공! Sent with message ID: ", messageId);
               callback();
               }
         });
      }
      else {
         callback();
      }
      }, function(err) {
         console.log('Push');
   });
});
   }, false, "Asia/Seoul");
// //경고 4,5
// new cron.CronJob('40 05 14 * * *', function(err){
// //    console.log("success!");
// UserModel.find({}, function(err, docs){
// //console.log('docs[0].Asset.set_date=', docs[0].Asset.set_date);
// async.eachSeries(docs, function(doc, callback) {
//    console.log('doc.Member.token=',doc.Member.token);
//     var message = {
//     registration_id: doc.Member.token, // required
//     collapse_key: ''+ Math.floor(Math.random() * 1000),
//    data: JSON.stringify({
//         result: 4,
//         body: doc.Asset.first_daily_budget+"원 사용 가능합니다.",
//         title: doc.Asset.first_daily_budget+"원 사용 가능합니다."
//        })
//     };
//     console.log('message=',message);
//     fcm.send(message, function(err, messageId){
//         if (err) {
//             console.log(doc.Asset.first_daily_budget+"사용 가능합니다. 에러!!");
//         } else {
//           console.log(doc.Asset.first_daily_budget+"사용 가능합니다.");
//         }
//       });
//     });
// });

// }, false, "Asia/Seoul");

// new cron.CronJob('00 * * * * *', function(err){
// UserModel.find({}, function(err, docs){
//    if(err) console.log('err=', err);
//       // console.log('docs=', docs);
//       async.eachSeries(docs, function(doc, callback) {
//          if(doc.Asset.daily_budget <= doc.Asset.first_daily_budget * 0.2){

//             var message1 = {
//             registration_id: doc.Member.token, // required
//             collapse_key: ''+Math.floor(Math.random()*1000),
//             data: {
//                body:"경고!!!! 오늘 하루 예산이 20% 남았습니다.",
//                   title:"경고!!!! 오늘 하루 예산이 20% 남았습니다."
//                   }
//             };

//             fcm.send(message1, function(err, messageId){
//                if (err) {
//                console.log("경고! Something has gone wrong!");
//                } else {
//                console.log("경고! Sent with message ID: ", messageId);
//                }
//                callback();
//             });
//          }

//          else if(doc.Asset.daily_budget <= 0){
//            var message2 = {
//             registration_id: doc.Member.token, // required
//             collapse_key: ''+Math.floor(Math.random()*1000),
//             data: {
//                body:"초과!! 비상금을 추가하세요",
//                   title:"초과!! 비상금을 추가하세요"
//                   }
//             };

//             fcm.send(message2, function(err, messageId){
//                if (err) {
//                console.log("초과! Something has gone wrong!");
//                } else {
//                console.log("초과! Sent with message ID: ", messageId);
//                }
//                callback();
//             });
//          }


//       else{
//          callback();
//       }
//    });
// });
// }, false, "Asia/Seoul");

//고정지출 확인 api
router.post('/fix_yn', function(req, res, next){
console.log('req.body=', req.body);
   var email = req.body.email;
   var fix_yn = req.body.fix_yn;
   var fix = req.body.fix;
   var index=0;

console.log("fix=",fix);
UserModel.findOne({'Member.email' : email}, function(err, doc){
   //문자내역이 고정지출이 맞다면, 고정지출 db를 수정하고, 지출내역에서 삭제
   if(fix_yn == 1){
      doc.Asset.budget = doc.Asset.budget + fix.msg_money;//지출로 처리한 것을 돌려놓음
      doc.Asset.daily_budget=doc.Asset.daily_budget+fix.msg_money;//지출로 처리한 것을 돌려놓음
      doc.Asset.total_ex_money=doc.Asset.total_ex_money+fix.msg_money;//지출로 처리한 것을 돌려놓음
      //지출내역 지우기**
      console.log("doc.Expenditure.Expense.length=",doc.Expenditure.Expense.length);
      for(var i=0; i<doc.Expenditure.Expense.length;i++){
         if(doc.Expenditure.Expense[i].money==fix.msg_money && doc.Expenditure.Expense[i].date==fix.msg_date && doc.Expenditure.Expense[i].time==fix.msg_time && doc.Expenditure.Expense[i].record==fix.msg_content){
             index=i;
             console.log("doc.Expenditure.Expense[",i,"].money=",doc.Expenditure.Expense[i].money,"fix.msg_money=",fix.msg_money,"index=",index);
         }//if
      }//for
      var db=doc.Expenditure.Expense;
      console.log("db=",db);
      db.splice(index,1);
      console.log("doc.Expenditure.Expense=",db);
      doc.Asset.first_month_budget = doc.Asset.first_month_budget + doc.Asset.total_f_money; //기존 월 예산에 기존 고정지출을 더함(고정지출 빼기전의 예산으로 만듬)
      doc.Asset.budget = doc.Asset.budget + doc.Asset.total_f_money; //기존 월 예산(변화한 예산)에 기존 고정지출을 더함(고정지출 빼기전의 예산으로 만듬)

      //db의 record가 fix_data와 같으면, 그 고정지출 금액을 수정해줌
      for (var i = 0; i<doc.Asset.Fix_ex.length;i++) {
         if(doc.Asset.Fix_ex[i].f_ex_record==fix.fix_data)
            doc.Asset.Fix_ex[i].f_ex_money = fix.msg_money;
      }
      //고정지출 금액 합계 계산
      var total_f_money=0;
      for (var i = 0 ; i < doc.Asset.Fix_ex.length ; i++) {
         total_f_money = total_f_money + Number(doc.Asset.Fix_ex[i].f_ex_money);
         console.log("total_f_money=",total_f_money);
      }
      doc.Asset.total_f_money = total_f_money;  //변경된 고정지출 합계을 db에 저장
      console.log("doc.Asset.Fix_ex=",doc.Asset.Fix_ex,"total_f_money=",total_f_money);
      doc.Asset.first_month_budget = doc.Asset.first_month_budget - doc.Asset.total_f_money; //월 예산에서 수정된 고정지출 뺌
      doc.Asset.budget = doc.Asset.budget - doc.Asset.total_f_money; //월 예산에서 수정된 고정지출 뺌
      doc.Asset.first_daily_budget = Math.floor(doc.Asset.first_month_budget / doc.Asset.d_day / 10 ) * 10; //새로운 월 예산을 기반으로 일예산 계산
      doc.Asset.daily_budget = doc.Asset.first_daily_budget - doc.Expenditure.total_ex_money;//일일 예산에서 하루 지출 금액을 뺌
      doc.save(function(err){
         if(err) {
               console.log('err=', err);
               res.json({result:0, error: err, msg :'고정 지출 저장 error!'});
            } else {
                console.log('doc=', doc);
                res.json({result:1, doc:doc, msg :'고정 지출 저장 success!'});
         }
      });
   }
   //문자내역이 고정지출이 아니라면 지출 내역 보여줌
   else{
       res.json({result:2, doc:doc, msg :'지출 내역'});
   }

   });
});

module.exports = router;