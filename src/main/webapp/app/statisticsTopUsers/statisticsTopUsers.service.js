(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('StatisticsTopUsers', StatisticsTopUsers);

    StatisticsTopUsers.$inject = ['$resource'];

    function StatisticsTopUsers ($resource) {
        var resourceUrl =  'api/statistics/users';

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
