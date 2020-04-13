import http from '@/utils/httpRequest.js'
export function policy() {
   return  new Promise((resolve,reject)=>{
        http({
<<<<<<< HEAD
            url: http.adornUrl("/oss/policy"),
=======
            url: http.adornUrl("/thirdparty/oss/policy"),
>>>>>>> 0169034b8c529c7a453a05a8ace5767c3f1acdb3
            method: "get",
            params: http.adornParams({})
        }).then(({ data }) => {
            resolve(data);
        })
    });
<<<<<<< HEAD
}
=======
}
>>>>>>> 0169034b8c529c7a453a05a8ace5767c3f1acdb3
