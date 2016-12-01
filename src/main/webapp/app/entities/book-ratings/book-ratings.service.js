(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('BookRatings', BookRatings);

    BookRatings.$inject = ['$resource'];

    function BookRatings ($resource) {
        var resourceUrl =  'api/book-ratings/:id';

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
