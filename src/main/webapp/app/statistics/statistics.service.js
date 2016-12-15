(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('Statistics', Statistics);

    Statistics.$inject = ['$resource'];

    function Statistics ($resource) {
        var resourceUrl =  'api/statistics/bookRatings';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
